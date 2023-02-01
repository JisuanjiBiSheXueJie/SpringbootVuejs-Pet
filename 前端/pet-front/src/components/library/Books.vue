/* eslint-disable */
<template>
  <div>
    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="70%"
      :before-close="handleClose">
      <amap :default-value="postForm.detailedAddress" @getPosition="getPosition" />
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
  </span>
    </el-dialog>
    <el-dialog
      title="评论信息"
      :visible.sync="CommentdialogVisible"
      width="50%"
      :before-close="handleClose">
      <el-card v-for="comment in commentsList" :key="comment.id">
          <el-row>
            <el-col :offset="1" :span="2" style="color: #36a3f7;font-size: 15px">{{comment.adoptname}}</el-col>
          </el-row>
          <el-row>
            <el-col :offset="1" :span="6" style="color: #d3dce6;font-size: 12px">  {{comment.upttime}}</el-col>
          </el-row>
        <el-row>
          {{comment.contents}}
        </el-row>
      </el-card>
      <span slot="footer" class="dialog-footer">
    <el-button @click="CommentdialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="CommentdialogVisible = false">确 定</el-button>
  </span>
    </el-dialog>
    <el-row>
      <el-col :span="6">
        <el-button  size="small" type="info" icon="el-icon-circle-plus-outline" @click="addAnimals">发布动物信息</el-button>
      </el-col>
      <el-col :span="12">
        <search-bar @onSearch="searchResult" ref="searchBar"></search-bar>
      </el-col>
    </el-row>
    <edit-form @onSubmit="listByArea" ref="edit"></edit-form>
    <el-row>
      <el-tooltip effect="light" placement="right"
                  v-for="item in animal.slice((currentPage-1)*pagesize,currentPage*pagesize)"
                  :key="item.id">
        <p slot="content" style="font-size: 14px;margin-bottom: 6px;">昵称：{{item.name}}</p>
        <p slot="content" style="font-size: 13px;margin-bottom: 6px">
          <span>年龄：{{item.age}}</span>
        </p>
        <p slot="content" style="width: 300px" class="abstract">{{item.description}}</p>
        <el-card style="width: 250px;margin-bottom: 20px;height: 280px;float: left;margin-right: 15px" class="book"
                 bodyStyle="padding:10px" shadow="hover">
          <el-row>
            <el-col :span="12">
              <div class="cover">
                <el-image v-if="item.picture === '' || item.picture == null">
                  <div slot="error" class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
                <img v-else :src="item.picture" alt="图片">
              </div>
            </el-col>
            <el-col :span="12">
              <div class="info">
                <div class="title">
                  <a href="">昵称：{{item.name === "" || item.name == null?"未起名字":item.name}}</a>
                  <div class="author">物种：{{item.breed}}</div>
                  <div class="author">
                    发现地址：{{item.address === "" || item.address == null?"未记录地址":item.address}}
                    <el-button icon="el-icon-location-information" circle @click="open(item.address)"></el-button>
                  </div>
                  <div class="author">发现时间：{{item.date}}</div>
                  <div>
                    <el-button v-if="item.status === '1'" type="text" @click="adoptAnimals(item)">我要领养</el-button>
                    <el-button v-if="item.status === '0'" type="text" @click="rescueAnimals(item)">一键救助</el-button>
                  </div>
                </div>
              </div>
              <el-button icon="el-icon-view" circle @click="openComment(item)"></el-button>
              <el-button icon="el-icon-chat-dot-square" circle @click="comment(item)"></el-button>
            </el-col>
          </el-row>
        </el-card>
      </el-tooltip>
    </el-row>
    <el-row>
      <el-pagination
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pagesize"
        :total="animal.length">
      </el-pagination>
    </el-row>
  </div>
</template>

<script>
import EditForm from './EditForm'
import SearchBar from './SearchBar'
import Amap from '../Amap'
export default {
  name: 'Books',
  components: {EditForm, SearchBar, Amap},
  data () {
    return {
      animal: [],
      currentPage: 1,
      pagesize: 17,
      dialogVisible: false,
      CommentdialogVisible: false,
      postForm: {
      detailedAddress: '',
        longitude: '',
        latitude: ''
      },
      commentsList: []
    }
  },
  mounted: function () {
    this.listByArea()
  },
  methods: {
    listByArea () {
      var _this = this
      var url = 'area/' + 1 + '/animal'
      this.$axios.get(url).then(resp => {
        if (resp && resp.status === 200) {
          _this.animal = resp.data.result
          _this.currentPage = 1
        }
      })
    },
    addAnimals () {
      this.$refs.edit.dialogFormVisible = true
    },
    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage
      // console.log(this.currentPage)
    },
    rescueAnimals (item) {
      item.status = '1'
      this.$axios
        .post('/animal', {
          id: item.id,
          name: item.name,
          picture: item.picture,
          status: item.status,
          date: item.date,
          address: item.address,
          age: item.age,
          breed: item.breed,
          description: item.description
        }).then(resp => {
        if (resp && resp.status === 200) {
          this.$alert('救助成功！')
          this.dialogFormVisible = false
          this.$emit('onSubmit')
        }
      })
    },
    adoptAnimals (item) {
      item.adoptname = window.localStorage.getItem('username')
      item.adoptname = item.adoptname.substring(1, item.adoptname.length - 1)
      item.status = '2'
      this.$axios
        .post('/animal', {
          id: item.id,
          name: item.name,
          picture: item.picture,
          status: item.status,
          date: item.date,
          address: item.address,
          age: item.age,
          breed: item.breed,
          adoptname: item.adoptname,
          description: item.description
        }).then(resp => {
        if (resp && resp.status === 200) {
          this.$alert('领养申请发送成功待审核！')
          this.dialogFormVisible = false
          this.$emit('onSubmit')
        }
      })
    },
    searchResult () {
      var _this = this
      this.$axios
        .get('/search?keywords=' + this.$refs.searchBar.keywords, {
        }).then(resp => {
        if (resp && resp.status === 200) {
          _this.books = resp.data.result
        }
      })
    },
    comment (item) {
      item.adoptname = window.localStorage.getItem('username')
      item.adoptname = item.adoptname.substring(1, item.adoptname.length - 1)
      this.$prompt('请输入评论', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        this.$axios
          .post('/comments', {
            contents: value,
            adoptname: item.adoptname,
            aid: item.id
          }).then(resp => {
          if (resp && resp.status === 200) {
            this.$alert('评论成功！')
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        })
      })
    },
    // 获取地址信息
    getPosition ({ address, lat, lng, name }) {
      this.postForm.detailedAddress = address
      this.postForm.longitude = String(lng)
      this.postForm.latitude = String(lat)
    },
    open (address) {
      this.postForm.detailedAddress = address
      this.dialogVisible = true
    },
    openComment (animal) {
      console.log(animal)
      var _this = this
      this.$axios.get('/commentsbyaid?aid=' + animal.id).then(resp => {
        if (resp && resp.status === 200) {
          _this.commentsList = resp.data.result
          _this.CommentdialogVisible = true
        }
      })
    }
  }
}
</script>
<style scoped>

.cover {
  width: 115px;
  height: 172px;
  margin-bottom: 7px;
  overflow: hidden;
  cursor: pointer;
}

img {
  width: 115px;
  height: 172px;
  /*margin: 0 auto;*/
}

.title {
  font-size: 14px;
  text-align: left;
  margin-left: 8px;
}

.author {
  color: #333;
  width: 102px;
  font-size: 13px;
  margin-bottom: 6px;
  margin-top: 5px;
  text-align: left;
}

.abstract {
  display: block;
  line-height: 17px;
}

.el-icon-delete {
  cursor: pointer;
  float: right;
}

.switch {
  display: flex;
  position: absolute;
  left: 780px;
  top: 25px;
}

a {
  text-decoration: none;
}

a:link, a:visited, a:focus {
  color: #3377aa;
}

</style>
