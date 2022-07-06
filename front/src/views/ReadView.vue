<script setup lang="ts">
import { onMounted, ref } from "@vue/runtime-core";
import axios from "axios";
import { useRouter } from "vue-router";

const props = defineProps({
  postId: {
    type: [Number, String],
    required: true,
  },
});

const post = ref({
  id: 0,
  title: "",
  content: "",
  createdTime: "",
  updateTime: "",
});

const router = useRouter();

const moveToEdit = () => {
  router.push({ name: "edit", params: { postId: props.postId } });
};

const deletePost = () => {
  axios.delete(`/api/posts/${props.postId}`).then(() => {
    router.replace({ name: "home" });
  });
};

onMounted(() => {
  axios.get(`/api/posts/${props.postId}`).then((response) => {
    post.value = response.data;
  });
});
</script>
<template>
  <el-row>
    <el-col>
      <h2 class="title">{{ post.title }}</h2>
      <div class="sub d-flex">
        <div class="category">개발</div>
        <div class="regDate">{{ post.createdTime }}</div>
        <div class="modDate" v-if="post.updateTime !== post.createdTime">
          {{ post.updateTime }}
        </div>
      </div>
    </el-col>
  </el-row>
  <el-row class="mt-3">
    <el-col>
      <div class="content">{{ post.content }}</div>
    </el-col>
  </el-row>

  <el-row>
    <el-col>
      <div class="d-flex justify-content-end">
        <el-button type="warning" @click="moveToEdit()">수정</el-button>
        <el-popconfirm
          title="Are you sure to delete this?"
          @confirm="deletePost()"
        >
          <template #reference>
            <el-button type="Danger">삭제</el-button>
          </template>
        </el-popconfirm>
      </div>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss">
.title {
  font-size: 1.6rem;
  font-weight: 600;
  color: #383838;
  margin: 0;
}

.sub {
  font-size: 0.78rem;
  margin-top: 10px;
  .regDate {
    margin-left: 10px;
    color: #6b6b6b;
  }
  .modDate {
    margin-left: 10px;
    color: #6b6b6b;
  }
}

.content {
  font-size: 0.95rem;
  margin-top: 8px;
  color: #7e7e7e;
  white-space: break-spaces;
  line-height: 1.5;
}
</style>
