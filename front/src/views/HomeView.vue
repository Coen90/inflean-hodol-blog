<script setup lang="ts">
import { ref } from "@vue/reactivity";
import axios from "axios";
import { useRouter } from "vue-router";

const router = useRouter();

const posts = ref([
  {
    id: 0,
    title: "",
    content: "",
    createdTime: "",
  },
]);
axios.get("/api/posts?page=1&size=5").then((response) => {
  posts.value.length = 0;
  response.data.forEach((r: any) => {
    posts.value.push(r);
  });
});

const moveToRead = () => {
  router.push({ name: "read" });
};
</script>

<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
      <div class="title">
        <router-link :to="{ name: 'read', params: { postId: post.id } }">{{
          post.title
        }}</router-link> 
      </div>

      <div class="content">{{ post.content }}</div>

      <div class="sub d-flex">
        <div class="category">개발</div>
        <div class="regDate">{{ post.createdTime }}</div>
      </div>
    </li>
  </ul>
</template>

<style scoped lang="scss">
ul {
  list-style: none;
  padding: 0;
}

li {
  margin-bottom: 2rem;

  .title {
    a {
      font-size: 1.1rem;
      color: #383838;
      text-decoration: none;
    }
    &:hover {
      text-decoration: underline;
    }
  }
  .content {
    font-size: 0.85rem;
    margin-top: 7px;
    color: #5d5d5d;
  }
  &:last-child {
    margin-bottom: 0;
  }

  .sub {
    margin-top: 7px;
    font-size: 0.78rem;
    margin-top: 5px;
    .regDate {
      margin-left: 10px;
      color: #6b6b6b;
    }
  }
}
</style>
