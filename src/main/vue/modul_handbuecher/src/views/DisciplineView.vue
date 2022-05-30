<template>
  <div>
    <ul class="list-group" v-if="model.searchComplete && model.classLoaded">
      <li class="list-group-item" v-for="discipline in disciplines" :key="discipline">
        <router-link :to="LinkAPI.getLink($route.params.faculty,discipline, undefined, undefined, $route.query.query)">{{ api.getLabel(model.classifications["discipline"], discipline, model.currentLang) }}</router-link>
      </li>
    </ul>
    <search />
  </div>
</template>

<script lang="ts">
import {Options, Vue} from 'vue-class-component';
import search from "@/components/Search.vue";
import {modelGlobal} from "@/Model";
import {ClassficationAPI} from "@/ClassficationAPI";
import { LinkAPI } from '@/LinkAPI';

Vue.registerHooks(["beforeRouteUpdate"])
@Options({
  components: {search}
})
export default class DisciplineView extends Vue {
  model = modelGlobal;
  disciplines = modelGlobal.disciplines;
  api = ClassficationAPI;
  LinkAPI=LinkAPI;

}
</script>
