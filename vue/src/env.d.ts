// src/env.d.ts
/// <reference types="vite/client" />

// 声明Vite环境变量的类型（解决编辑器提示）
interface ImportMetaEnv {
    // 声明你的VITE_BASE_URL
    readonly VITE_BASE_URL: string;
    // 可选：声明其他环境变量
    readonly VITE_API_PREFIX: string;
}

// 扩展ImportMeta类型
interface ImportMeta {
    readonly env: ImportMetaEnv;
}