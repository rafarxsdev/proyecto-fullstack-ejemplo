module.exports = {
  content: [
    "./src/**/*.{html,ts}"
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        primary: {
          '50': '#ebfeff',
          '100': '#cefaff',
          '200': '#a2f3ff',
          '300': '#63e8fd',
          '400': '#1cd2f4',
          '500': '#00b4d8',
          '600': '#0390b7',
          '700': '#0a7394',
          '800': '#125d78',
          '900': '#144d65',
          '950': '#063346',
        },
        secundary: {
          '50': '#f5f7ee',
          '100': '#e9ecdb',
          '200': '#ccd5ae',
          '300': '#b8c492',
          '400': '#9cad6e',
          '500': '#809250',
          '600': '#63733d',
          '700': '#4d5932',
          '800': '#3f482c',
          '900': '#373f28',
          '950': '#1c2112',
        },
        neutral: {
          '50': '#f6f6f7',
          '100': '#efeff0',
          '200': '#e1e2e4',
          '300': '#cecfd3',
          '400': '#b9bac0',
          '500': '#a6a6ae',
          '600': '#9999a1',
          '700': '#7e7d85',
          '800': '#67666d',
          '900': '#55555a',
          '950': '#323234',
        }
      }
    },
    fontFamily: {
      'body': [
        'Inter',
        'ui-sans-serif',
        'system-ui',
        '-apple-system',
        'system-ui',
        'Segoe UI',
        'Roboto',
        'Helvetica Neue',
        'Arial',
        'Noto Sans',
        'sans-serif',
        'Apple Color Emoji',
        'Segoe UI Emoji',
        'Segoe UI Symbol',
        'Noto Color Emoji'
      ],
      'sans': [
        'Inter',
        'ui-sans-serif',
        'system-ui',
        '-apple-system',
        'system-ui',
        'Segoe UI',
        'Roboto',
        'Helvetica Neue',
        'Arial',
        'Noto Sans',
        'sans-serif',
        'Apple Color Emoji',
        'Segoe UI Emoji',
        'Segoe UI Symbol',
        'Noto Color Emoji'
      ]
    },
    plugins: []
  }
}
