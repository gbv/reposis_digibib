<template>
  <div class="search-component" v-if="model.searchComplete && model.classLoaded">
    <nav v-if="pages.pages.length>1">
      <ul class="pagination justify-content-center">
        <li :class="`page-item ${pages.currentPage > 0? '' : 'disabled'}`">
          <router-link class="page-link"
                       :to="LinkAPI.getLink($route.params.faculty, $route.params.discipline, $route.params.subject,(pages.currentPage-1)*pages.pageSize, $route.query.query)">
            &laquo;
          </router-link>

        </li>
        <li :class="`page-item ${pages.currentPage == page ? 'active' : ''}`" v-for="page in pages.pages" :key="page">
          <router-link class="page-link"
                       :to="LinkAPI.getLink($route.params.faculty, $route.params.discipline, $route.params.subject,page*pages.pageSize, $route.query.query)">
            {{ page+1 }}
          </router-link>
        </li>
        <li :class="`page-item ${pages.currentPage < pages.maxPages ? '': 'disabled'}`">
          <router-link class="page-link"
                       :to="LinkAPI.getLink($route.params.faculty, $route.params.discipline, $route.params.subject,(pages.currentPage+1)*pages.pageSize, $route.query.query)">
            &raquo;
          </router-link>
        </li>
      </ul>
    </nav>
    <div class="row">
      <div class="col-md-6" v-for="doc in model.search.response.docs" :key="doc.id">
        <div class="card">
          <!-- <img class="card-img-bottom"
               :src="`${model.baseURL}api/iiif/image/v2/thumbnail/${doc.id}/full/!255,255/0/default.png`"
               alt="Card image cap"> -->
          <div class="card-body">
            <h5 class="card-title">
              <a :href="`${model.baseURL}receive/${doc['id']}`">
                <template v-for="title in doc['mods.title']">{{ title }}</template>
              </a>
            </h5>
            <h6 class="card-subtitle mb-2 text-muted">
              <template v-for="genre in doc['mods.genre']">
                {{ classApi.getLabel(model.classifications["mir_genres"], genre, model.currentLang) }}
              </template>
            </h6>
            <p class="card-text"></p>
          </div>
        </div>
      </div>
    </div>
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
    const pageSize = 10;

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

.search-component {
  margin-top: 2em;
  margin-bottom: 2em;
}

.card {
  margin-bottom: 2em;
}

img{
  max-height: 200px;
  max-width: 200px;
}

ul {
  margin-top: 1em;
}
</style>
