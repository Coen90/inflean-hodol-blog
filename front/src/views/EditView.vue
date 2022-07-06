<script setup lang="ts">
import { ref } from "vue";

import axios from "axios";
import { useRouter } from "vue-router";

const router = useRouter();

const post = ref({
  id: 0,
  title: "",
  content: "",
});

const props = defineProps({
  postId: {
    type: [Number, String],
    required: true,
  },
});

axios.get(`/api/posts/${props.postId}`).then((response) => {
  post.value = response.data;
});

const edit = () => {
  axios.patch(`/api/posts/${props.postId}`, post.value).then(() => {
    router.replace({ name: "home" });
  });
};

const cancel = () => {
  router.push({ name: "read", params: { postId: props.postId } });
};
</script>
<template>
  <div>
    <el-input
      type="text"
      v-model="post.title"
      placeholder="제목을 입력해주세요."
      maxlength="100"
      show-word-limit
    />
  </div>

  <div class="mt-2">
    <el-input type="textarea" v-model="post.content" rows="15"></el-input>
  </div>

  <div class="mt-2">
    <div class="d-flex justify-content-end">
      <el-button type="warning" @click="edit()">수정 완료</el-button>
      <el-button type="info" @click="cancel()">취소</el-button>
    </div>
  </div>
</template>

<style></style>
