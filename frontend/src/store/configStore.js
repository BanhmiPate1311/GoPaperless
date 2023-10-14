import { combineReducers, configureStore } from "@reduxjs/toolkit";
import thunk from "redux-thunk";
import { apiControllerManagerReducer } from "./apiControllerManager";
import { participantControllerManagerReducer } from "./participantControllerManager";
import { prefixControllerReducer } from "./prefixController";
import { electronicControllerManagerReducer } from "./electronicControllerManager";
const rootReducer = combineReducers({
  apiControllerManagerReducer,
  participantControllerManagerReducer,
  prefixControllerReducer,
  electronicControllerManagerReducer,
});

export const store = configureStore({
  reducer: rootReducer,
  middleware: [thunk],
  devTools: true,
});
