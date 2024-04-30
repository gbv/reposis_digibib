module.exports = {
  root: true,
  plugins: ['vue', '@typescript-eslint'],
  env: {
    node: true,
    'vue/setup-compiler-macros': true,
  },
  extends: [
    'eslint:recommended',
    'plugin:vue/vue3-recommended',
    'plugin:@typescript-eslint/recommended',
  ],
  parser: 'vue-eslint-parser',
  parserOptions: {
    ecmaVersion: 2020,
    parser: '@typescript-eslint/parser',
    sourceType: 'module',
  },
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    '@typescript-eslint/no-namespacer': 'off',
    'vue/multi-word-component-names': 'off',
    'max-len': ['error', { code: 100 }],
    'no-mixed-spaces-and-tabs': 'error',
    'no-multi-spaces': 'error',
  },
};
