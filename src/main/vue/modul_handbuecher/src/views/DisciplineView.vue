<template>
  <div>
    <ul class="mm-category-selection" v-if="model.searchComplete && model.classLoaded">
      <li class="level-2 active" v-for="discipline in disciplines" :key="discipline">
        <router-link
          class=""
          :to="LinkAPI.getLink($route.params.faculty,discipline, undefined, undefined, $route.query.query)">
          {{ api.getLabel(model.classifications["discipline"], discipline, model.currentLang) }}
        </router-link>
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import {Options, Vue} from 'vue-class-component';
import {modelGlobal} from "@/Model";
import {ClassficationAPI} from "@/ClassficationAPI";
import { LinkAPI } from '@/LinkAPI';

Vue.registerHooks(["beforeRouteUpdate"])
@Options({
  components: {}
})
export default class DisciplineView extends Vue {
  model = modelGlobal;
  disciplines = modelGlobal.disciplines;
  api = ClassficationAPI;
  LinkAPI=LinkAPI;

}
</script>
