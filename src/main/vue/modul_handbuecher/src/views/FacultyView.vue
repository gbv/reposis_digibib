<template>
  <div>
    <ul class="mm-category-selection" v-if="model.searchComplete">
      <li class="level-1 active" v-for="faculty in model.faculties" :key="faculty">
        <router-link
          class=""
          :to="LinkAPI.getLink(faculty, undefined, undefined, undefined, $route.query.query)">
          {{ api.getLabel(model.classifications["mir_institutes"], faculty, model.currentLang) }}
        </router-link>
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import {Options, Vue} from 'vue-class-component';
import {modelGlobal} from "@/Model";
import {ClassficationAPI} from "@/ClassficationAPI";
import {LinkAPI} from "@/LinkAPI";

@Options({
  components: {},
})
export default class FacultyView extends Vue {
  api = ClassficationAPI;
  model = modelGlobal;
  LinkAPI = LinkAPI;
}
</script>
