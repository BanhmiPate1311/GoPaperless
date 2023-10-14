import axios from "axios";

// // const baseURL = "http://localhost:8080/signing";
// const baseURL = "/signing";

// export const api = axios.create({
//   headers: {
//     "Content-Type": "application/json",
//   },
//   baseURL,
// });

// api.interceptors.request.use((config) => {
//   config = {
//     ...config,
//     headers: {
//       "Content-Type": "application/json",
//     },
//     baseURL,
//   };
//   return config;
// });

export const api = axios.create({
  // local
  // baseURL: "/signing",
  baseURL: "http://localhost:8080/signing",
  headers: {
    "Content-Type": "application/json",
  },
});
