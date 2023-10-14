import { usePrefixController } from "../store/prefixController";

export const useConnectorList = (setConnectorList) => {
  const list = [];
  const { connectorNames } = usePrefixController();
  list.push(connectorNames);
  setConnectorList(list);
};
