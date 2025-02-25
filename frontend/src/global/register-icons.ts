import * as ElementPlusIconsVue from '@element-plus/icons-vue';
import type { App } from 'vue';

// 全局注册 ElementPlus 图标
function elementPlusIcons(app: App<Element>) {
  for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component);
  }
}

export default elementPlusIcons;
