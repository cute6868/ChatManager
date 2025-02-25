// 引入 normalize.css 重置浏览器样式
import 'normalize.css';

// 引入自定义样式
import './assets/css/main.scss';

// 创建应用程序
import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import pinia from './store';
import elementPlusIcons from './global/register-icons';

const app = createApp(App);
app.use(router);
app.use(pinia);
app.use(elementPlusIcons);
app.mount('#app');
