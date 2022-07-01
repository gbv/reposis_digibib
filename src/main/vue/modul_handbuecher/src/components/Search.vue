<template>
  <div class="search-component" v-if="model.searchComplete && model.classLoaded">

    <div class="mm-hit-count">
      {{pages.numFound}}
      <span v-if="pages.numFound === 1">Handbuch</span>
      <span v-if="pages.numFound !== 1">Handbücher</span>
      gefunden
    </div>

    <ul class="mm-results">
      <li v-for="doc in model.search.response.docs" :key="doc.id">
        <a :href="`${model.baseURL}receive/${doc['id']}`">
          <template v-for="title in doc['mods.title']">{{ title }}</template>
        </a>
      </li>
    </ul>

    <nav v-if="pages.pages.length>1">
      <ul class="pagination justify-content-center">
        <li :class="`page-item ${pages.currentPage > 0? '' : 'disabled'}`">
          <router-link class="page-link"
                       :to="LinkAPI.getLink($route.params.faculty, $route.params.discipline, $route.params.subject,(pages.currentPage-1)*pages.pageSize)">
            &laquo;
          </router-link>

        </li>
        <li :class="`page-item ${pages.currentPage == page ? 'active' : ''}`" v-for="page in pages.pages" :key="page">
          <router-link class="page-link"
                       :to="LinkAPI.getLink($route.params.faculty, $route.params.discipline, $route.params.subject,page*pages.pageSize)">
            {{ page+1 }}
          </router-link>
        </li>
        <li :class="`page-item ${pages.currentPage < pages.maxPages ? '': 'disabled'}`">
          <router-link class="page-link"
                       :to="LinkAPI.getLink($route.params.faculty, $route.params.discipline, $route.params.subject,(pages.currentPage+1)*pages.pageSize)">
            &raquo;
          </router-link>
        </li>
      </ul>
    </nav>

  </div>
</template>

<script lang="ts">
import {Options, Vue} from 'vue-class-component';
import {modelGlobal} from "@/Model";
import {ClassficationAPI} from "@/ClassficationAPI";
import {LinkAPI} from "@/LinkAPI";


@Options({})
export default class Search extends Vue {
  model = modelGlobal;
  classApi = ClassficationAPI;
  LinkAPI=LinkAPI;

  get pages() {
    if (!this.model.searchComplete) {
      return {};
    }

    const numFound = this.model.search.response.numFound;
    const start = this.model.search.response.start;
    const pageSize = 20;

    const maxPages = Math.floor(numFound / pageSize);
    const currentPage = Math.ceil(start / pageSize);

    const pages = [];
    for (let i = Math.max(currentPage - 5, 0); i < currentPage; i++) {
      pages.push(i);
    }

    pages.push(currentPage);

    for(let i = currentPage+1; i <= Math.min(currentPage + 5, maxPages); i++){
      pages.push(i);
    }
    let newVar = {
      currentPage,
      pages,
      pageSize,
      maxPages,
      start,
      numFound
    };

    console.log(newVar);

    return newVar;
  }

}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
