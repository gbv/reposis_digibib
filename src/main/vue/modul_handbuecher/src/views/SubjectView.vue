<template>
  <div>
    <ul class="mm-category-selection" v-if="model.searchComplete">
      <li class="level-3 active" v-for="subject in model.subjects" :key="subject">
        <router-link
          class=""
          :to="LinkAPI.getLink($route.params.faculty, $route.params.discipline, subject, undefined, $route.query.query)">
          {{subject}}
        </router-link>
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import {Options, Vue} from 'vue-class-component';
import {modelGlobal} from "@/Model";
import { LinkAPI } from '@/LinkAPI';

@Options({
  components: {},
})
export default class SubjectView extends Vue {
  model = modelGlobal;
  LinkAPI=LinkAPI;
}
</script>
