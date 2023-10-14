import i18n from "i18next";
import Backend from "i18next-http-backend";
import { initReactI18next } from "react-i18next";

// import translationEN from "../translation/eng.translation";
// import translationVI from "../translation/vi.translation";
// import translationGermany from "../translation/germany.translation";
// import translationChina from "../translation/china.translation";
// import translationEstonian from "../translation/estonian.translation";
// import translationRussian from "../translation/russian.translation";
import translationEN from "../translation/en.json";
import translationVI from "../translation/vi.json";
import translationGermany from "../translation/germany.json";
import translationChina from "../translation/china.json";
import translationEstonian from "../translation/estonian.json";
import translationRussian from "../translation/russian.json";

// the translations
const resources = {
  0: {
    translation: translationEN,
  },
  1: {
    translation: translationVI,
  },
  2: {
    translation: translationGermany,
  },
  3: {
    translation: translationChina,
  },
  4: {
    translation: translationEstonian,
  },
  5: {
    translation: translationRussian,
  },
};

i18n
  .use(Backend)
  .use(initReactI18next)
  .init({
    resources,
    fallbackLng: "0", // Ngôn ngữ dự phòng nếu ngôn ngữ hiện tại không có sẵn
    debug: true,
    interpolation: {
      escapeValue: false, // not needed for react as it escapes by default
    },
  });

export default i18n;
