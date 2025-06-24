import MarkdownIt from 'markdown-it';
import hljs from 'highlight.js';
import DOMPurify from 'dompurify';
import katex from 'katex';
import MarkdownItKatex from 'markdown-it-katex';
import MarkdownItFootnote from 'markdown-it-footnote';

export default function useMarkdownHandler() {
  // 初始化Markdown解析器
  const md = MarkdownIt({
    html: true, // 允许解析html标签
    linkify: true, // 自动识别链接
    typographer: true, // 智能排版
    breaks: false, // 转换换行符为<br>
    // 代码高亮配置
    highlight: (str, lang) => {
      if (lang && hljs.getLanguage(lang)) {
        // 保持 GitHub 风格的代码高亮
        return `<pre class="hljs"><code class="language-${lang}">${hljs.highlight(str, { language: lang }).value}</code></pre>`;
      }
      return `<pre><code>${str}</code></pre>`; // 未指定语言时不高亮
    }
  });

  // 注册脚注插件
  md.use(MarkdownItFootnote);

  // 注册Katex插件来支持数学公式
  md.use(MarkdownItKatex, {
    delimiters: 'dollars', // 使用$$包裹公式
    throwOnError: false,
    errorColor: '#cc0000',
    escape: false
  });

  // 自定义图片渲染规则（添加懒加载）
  md.renderer.rules.image = (tokens, idx, options, env, self) => {
    const token = tokens[idx]; // 获取当前token
    const srcIndex = token.attrIndex('src'); // 获取src属性的索引
    // 如果有src属性，则修改为data-src实现懒加载
    if (srcIndex >= 0 && token.attrs) {
      token.attrs[srcIndex][0] = 'data-src'; // 改为data-src实现懒加载
      token.attrPush(['loading', 'lazy']); // 添加原生懒加载属性
    }
    return self.renderToken(tokens, idx, options);
  };

  // 自定义Katex渲染规则，确保公式正确显示
  md.renderer.rules.katex_inline = (tokens, idx) => {
    const formula = tokens[idx].content;
    try {
      return katex.renderToString(formula, {
        throwOnError: false,
        errorColor: '#cc0000'
      });
    } catch (e) {
      return `<span class="katex-error">${e.message}</span>`;
    }
  };

  md.renderer.rules.katex_block = (tokens, idx) => {
    const formula = tokens[idx].content;
    try {
      return `<div class="katex-block">${katex.renderToString(formula, {
        throwOnError: false,
        errorColor: '#cc0000',
        displayMode: true
      })}</div>`;
    } catch (e) {
      return `<div class="katex-error">${e.message}</div>`;
    }
  };

  // 解析Markdown并清理XSS的函数
  const parseMarkdown = (text: string) => {
    if (!text) return '';
    const html = md.render(text);
    return DOMPurify.sanitize(html);
  };

  return {
    parseMarkdown
  };
}
