import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import FacultyView from '../views/FacultyView.vue'
import DisciplineView from "@/views/DisciplineView.vue";
import SubjectView from "@/views/SubjectView.vue";
import PlainView from "@/views/PlainView.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'faculty',
    component: FacultyView
  },
  {
    path: '/:faculty',
    name: 'discipline',
    component: DisciplineView
  },
  {
    path: '/:faculty/:discipline',
    name: 'subject',
    component: SubjectView
  },
  {
    path: '/:faculty/:discipline/:subject',
    name: "genre",
    component: PlainView
  }
]

function getContext() {
  const el = document.createElement('a');
  el.href = (<any>window).webApplicationBaseURL;
  return el.pathname;
}

const router = createRouter({
  history: createWebHistory(getContext() + "handbuecher/"),
  routes
})

export default router
