import { combineReducers, configureStore } from "@reduxjs/toolkit";
import thunk from "redux-thunk";
import { apiControllerManagerReducer } from "./apiControllerManager";
const rootReducer = combineReducers({
  apiControllerManagerReducer,
});

export const store = configureStore({
  reducer: rootReducer,
  middleware: [thunk],
  devTools: true,
});
