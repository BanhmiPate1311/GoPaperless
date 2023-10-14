import React from "react";
import { useRoutes } from "react-router-dom";
import MainLayout from "../layouts/MainLayout";
import { Batch, Home, NotFound, Open, Sequence, Signing } from "../pages";
import Validation from "../pages/Validation";

const Routers = () => {
  const routing = useRoutes([
    {
      path: "/",
      element: <MainLayout />,
      children: [
        {
          path: "/",
          element: <Home />,
        },
        {
          path: "/home",
          element: <Home />,
        },
        {
          path: "/signing",
          element: <Signing />,
        },
        {
          path: "/signing/:signing_token",
          element: <Signing />,
        },
        {
          path: "/signing/batch/:batch_token",
          element: <Batch />,
        },
        {
          path: "/signing/sequence/:batch_token",
          element: <Sequence />,
        },
        {
          path: "/signing/sequence/:batch_token/result",
          element: <Batch />,
        },
        {
          path: "/signing/open/:upload_token",
          element: <Open />,
        },
        {
          path: "/signing/validation/:upload_token",
          element: <Validation />,
        },
        {
          path: "/signing/notFound",
          element: <NotFound />,
        },
        {
          path: "/signing/*",
          element: <NotFound />,
        },
      ],
    },
  ]);
  return routing;
};

export default Routers;
