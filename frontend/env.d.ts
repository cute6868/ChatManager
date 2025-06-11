/// <reference types="vite/client" />

// src/types/markdown-it-footnote.d.ts
declare module 'markdown-it-footnote' {
  import MarkdownIt from 'markdown-it';

  // 声明插件函数（简化版，忽略具体参数类型）
  export default function markdownItFootnote(md: MarkdownIt, options?: any): void;
}

// src/types/markdown-it-katex.d.ts
declare module 'markdown-it-katex' {
  import MarkdownIt from 'markdown-it';
  import { KatexOptions } from 'katex';

  // 定义插件选项接口
  interface MarkdownItKatexOptions extends KatexOptions {
    errorColor?: string;
    delimiters?: 'dollars' | 'mathjax' | 'displaymath';
    strict?: boolean;
    throwOnError?: boolean;
  }

  // 声明插件函数
  export default function markdownItKatex(md: MarkdownIt, options?: MarkdownItKatexOptions): void;
}
