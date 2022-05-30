<template>
  <div>
    <ul class="list-group" v-if="model.searchComplete">
      <li class="list-group-item" v-for="faculty in model.faculties" :key="faculty">
        <router-link :to="LinkAPI.getLink(faculty, undefined, undefined, undefined, $route.query.query)">
          {{ api.getLabel(model.classifications["mir_institutes"], faculty, model.currentLang) }}
        </router-link>
      </li>
    </ul>
    <search />

  </div>
</template>

<script lang="ts">
import {Options, Vue} from 'vue-class-component';
import Search from "@/components/Search.vue";
import {modelGlobal} from "@/Model";
import {ClassficationAPI} from "@/ClassficationAPI";
import {LinkAPI} from "@/LinkAPI";

@Options({
  components: {Search},
})
export default class FacultyView extends Vue {
  api = ClassficationAPI;
  model = modelGlobal;
  LinkAPI = LinkAPI;
}
</script>
