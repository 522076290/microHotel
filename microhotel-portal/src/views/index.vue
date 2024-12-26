<template>
    <div>
      <el-container>
        <!-- Main Section -->
        <el-main>
          <div class="hero-section">
            <h1>希望给您留下美好的回忆</h1>
          </div>
          <el-row class="info-box" type="flex" justify="center">
            <p>1. 本站为演示站，<a href="#">详细介绍地址点击</a>...</p>
            <p>2. 用户登录账户...</p>
            <p>3. 需要完整代码联系...</p>
          </el-row>
  
          <!-- Room Categories -->
          <el-row :gutter="20">
            <el-col :span="12" v-for="(room, index) in rooms" :key="index">
              <el-card>
                <img :src="room.image" alt="Room Image" />
                <div>{{ room.name }}</div>
              </el-card>
            </el-col>
          </el-row>
        </el-main>
      </el-container>
    </div>
</template>
  
  <script>
  export default {
    data() {
      return {
        rooms: [
          { name: "大床房", image: "path-to-image.jpg" },
          { name: "双床房", image: "path-to-image.jpg" },
          // Add more room types here
        ],
      };
    },
  };
  </script>
  
  <style scoped>
  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
    background: #f5f5f5;
  }
  
  .hero-section {
    text-align: center;
    margin: 20px 0;
  }
  
  .info-box {
    margin: 20px;
    background: #fdfdfd;
    padding: 10px;
    border: 1px solid #eee;
  }
  
  el-card {
    text-align: center;
  }
  
  el-card img {
    width: 100%;
    height: 200px;
    object-fit: cover;
  }
  </style>
  