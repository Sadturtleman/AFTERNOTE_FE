module.exports = {
  extends: ['@commitlint/config-conventional'],
  rules: {
    'header-max-length': [2, 'always', 100],
    'body-max-line-length': [2, 'always', 100],
    'footer-max-line-length': [2, 'always', 100],
    // Allow PascalCase/English in subject (e.g. "BottomNavigation 관련 컴포넌트 패키지 이동")
    'subject-case': [0],
  },
};