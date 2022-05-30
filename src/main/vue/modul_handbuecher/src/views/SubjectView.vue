<template>
  <div>
    <ul class="list-group" v-if="model.searchComplete">
      <li class="list-group-item" v-for="subject in model.subjects" :key="subject">
        <router-link :to="LinkAPI.getLink($route.params.faculty, $route.params.discipline, subject, undefined, $route.query.query)">{{
            subject
          }}
        </router-link>
      </li>
    </ul>
    <search/>
  </div>
</template>

<script lang="ts">
import {Options, Vue} from 'vue-class-component';
import Search from "@/components/Search.vue";
import {modelGlobal} from "@/Model";
import { LinkAPI } from '@/LinkAPI';

@Options({
  components: {Search},
})
export default class SubjectView extends Vue {
  model = modelGlobal;
  LinkAPI=LinkAPI;
}
</script>
