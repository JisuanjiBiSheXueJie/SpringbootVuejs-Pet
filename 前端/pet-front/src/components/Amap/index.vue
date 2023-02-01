<template>
  <div id="amap-container">
    <el-input
      id="search-input"
      v-model="searchValue"
      class="input-with"
      placeholder="请输入地址"
      clearable
      @clear="handelclearInput"
      @keyup.native.enter="handelSearch"
    >
      <el-button
        slot="append"
        size="small"
        type="primary"
        icon="el-icon-search"
        @click="handelSearch"
      >搜索</el-button
      >
    </el-input>

    <div id="searchResultPanel" />
    <el-row class="margin-top-10 address">
      当前地址是： {{ formattedAddress }}
      <el-button size="small" type="primary" @click="handelSave"
      >使用该地址</el-button
      >
    </el-row>

    <div id="custom-amap" />
  </div>
</template>

<script>
const AMap = window.AMap
export default {
  name: 'AMap',
  props: {
    defaultValue: {
      type: String,
      default: ''
    }
  },
  data () {
    return {
      defaultCity: '北京',
      // 地图对象
      map: null,
      // 定位默认地址 | 搜索后选择的地址
      formattedAddress: null,
      // 地址对应的经纬度信息
      position: {},
      // 检索关键字
      searchValue: '',
      // 检索函数对象
      placeSearch: null,
      // 检索结果数据数据
      searchInfoList: [],
      // 地图标记
      marker: '',
      // 地址解析（正向）
      geocoder: '',
      // 地址名称
      name: ''
    }
  },
  watch: {
    defaultValue () {
      this.searchValue = this.defaultValue
    }
  },
  mounted () {
    // 初始化地图页面
    this.initMap()
  },
  beforeDestroy () {
    // 销毁地图
    this.map.destroy()
  },
  methods: {
    //   初始化地图页面
    initMap () {
      this.map = new AMap.Map('custom-amap', {
        resizeEnable: true,
        zoom: 50
      })
      // 添加工具栏
      this.map.plugin(['AMap.ToolBar', 'AMap.Scale', 'AMap.OverView'], () => {
        // 工具条
        const toolbar = new AMap.ToolBar()
        // 比例尺
        const scale = new AMap.Scale()
        // 显示鹰眼
        const overView = new AMap.OverView()
        this.map.addControl(toolbar)
        this.map.addControl(scale)
        this.map.addControl(overView)
      })
      // 添加maker
      this.setMaker()
      // 添加鼠标点选地图选择地址
      this.addAmapGeocoder()
      // 添加定位
      this.addAMapGeolocation()
      // 添加检索提示
      this.addAMapAutocompletePlaceSearch()
    },
    // 添加maker
    setMaker () {
      this.marker = new AMap.Marker()
      this.map.add(this.marker)
      // 添加解析地理位置插件
      this.map.plugin('AMap.Geocoder', () => {
        // 异步加载插件
        this.geocoder = new AMap.Geocoder({
          city: this.defaultCity, // 默认：“全国”
          radius: 1000 // 范围，默认：500
        })
      })
    },
    // 添加鼠标点选地图选择地址
    addAmapGeocoder () {
      // 添加maker
      this.setMaker()
      // 地图添加点击事件
      this.map.on('click', e => {
        const lnglat = [e.lnglat.lng, e.lnglat.lat]
        this.marker.setPosition(lnglat)
        this.geocoder.getAddress(lnglat, (status, result) => {
          if (status === 'complete' && result.regeocode) {
            const res = result.regeocode
            const data = {
              // 地址名称
              address: res.formattedAddress,
              // 纬度lat
              lat: lnglat[1],
              // 经度lng
              lng: lnglat[0]
            }
            this.formattedAddress = res.formattedAddress
            this.position = data
          } else {
            alert(JSON.stringify(result))
          }
        })
      })
    },
    // 添加自动定位
    addAMapGeolocation () {
      this.map.plugin('AMap.Geolocation', () => {
        const geolocation = new AMap.Geolocation({
          // 是否使用高精度定位，默认：true
          enableHighAccuracy: true,
          // 设置定位超时时间，默认：无穷大
          timeout: 10000,
          // 定位按钮的停靠位置的偏移量，默认：Pixel(10, 20)
          buttonOffset: new AMap.Pixel(200, 200),
          //  定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
          zoomToAccuracy: true,
          //  定位按钮的排放位置,  RB表示右下
          buttonPosition: 'RB'
        })
        // 获取当前位置
        geolocation.getCurrentPosition()
        // 添加定位当前城市成功监听
        AMap.event.addListener(
          geolocation,
          'complete',
          this.onCurrentPositionComplete
        )
        // 添加定位当前城市发生错误监听
        AMap.event.addListener(
          geolocation,
          'error',
          this.onCurrentPositionError
        )
      })
    },
    // 添加检索提示检索
    addAMapAutocompletePlaceSearch () {
      // 自动提示
      this.map.plugin('AMap.Autocomplete', () => {
        const auto = new AMap.Autocomplete({
          city: this.defaultCity,
          input: 'search-input'
        })
        // 添加检索监听
        AMap.event.addListener(auto, 'select', this.onSelectAutocomplete)
      })
      // 检索服务
      AMap.service(['AMap.PlaceSearch'], () => {
        // 构造地点查询类
        this.placeSearch = new AMap.PlaceSearch({
          type: '', // 兴趣点类别
          pageSize: 5, // 单页显示结果条数
          pageIndex: 1, // 页码
          city: this.defaultCity, // 兴趣点城市
          citylimit: false, // 是否强制限制在设置的城市内搜索
          map: this.map, // 展现结果的地图实例
          panel: 'searchResultPanel', // 结果列表将在此容器中进行展示。
          autoFitView: true // 是否自动调整地图视野使绘制的 Marker点都处于视口的可见范围
        })
      })
      // 添加检索监听
      AMap.event.addListener(
        this.placeSearch,
        'listElementClick',
        this.onSelectSearch
      )
    },
    // 定位当前城市成功回调
    onCurrentPositionComplete (res) {
      // 添加maker
      this.setMaker()
      const lnglat = [res.position.lng, res.position.lat]
      this.marker.setPosition(lnglat)
      console.log(res, 'res')
      this.formattedAddress = res.formattedAddress
      this.position = res.position
    },
    // 定位当前城市发生错误回调
    onCurrentPositionError (err) {
      console.log(err)
    },
    // 按钮触发检索
    handelSearch () {
      this.placeSearch.search(this.searchValue, (status, info) => {
        this.searchInfoList = info.poiList.pois
      })
    },
    // 选择自动提示数据事件回调
    onSelectAutocomplete (e) {
      this.searchValue = e.poi.name
      this.handelSearch()
    },
    // 选择检索数据结果事件回调
    onSelectSearch (e) {
      const res = e.data
      this.formattedAddress = res.cityname + res.adname + res.address
      this.name = res.name
      this.position = res.location
    },
    // 清除input里的值，清除搜索结果，再次初始化map
    handelclearInput () {
      document.querySelector('#searchResultPanel').innerHTML = ''
    },
    // 保存当前选择的地址,分发事件
    handelSave () {
      this.searchValue = this.formattedAddress
      const { lat, lng } = this.position
      const data = {
        name: this.name,
        // 地址名称
        address: this.formattedAddress,
        // 纬度lat
        lat,
        // 经度lng
        lng
      }
      this.$emit('getPosition', data)
    }
  }
}
</script>

<style lang="scss">
#amap-container {
  .el-input__clear {
    line-height: 34px;
    // top: 20px;
  }
  #custom-amap {
    height: 60vh;
    width: 100%;
    margin-top: 10px;
    border: 1px solid #ccc;
  }
  .input-with {
    // position: fixed;
    // top: 40px;
    z-index: 1;
    width: 580px;
  }
  .address {
    color: #373737;
  }
}
.amap-sug-result {
  z-index: 99999;
}
</style>
