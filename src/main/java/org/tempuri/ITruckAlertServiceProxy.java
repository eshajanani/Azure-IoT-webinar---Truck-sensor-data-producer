package org.tempuri;

public class ITruckAlertServiceProxy implements org.tempuri.ITruckAlertService {
  private String _endpoint = null;
  private org.tempuri.ITruckAlertService iTruckAlertService = null;
  
  public ITruckAlertServiceProxy() {
    _initITruckAlertServiceProxy();
  }
  
  public ITruckAlertServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initITruckAlertServiceProxy();
  }
  
  private void _initITruckAlertServiceProxy() {
    try {
      iTruckAlertService = (new org.tempuri.TruckAlertServiceLocator()).getBasicHttpsBinding_ITruckAlertService();
      if (iTruckAlertService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iTruckAlertService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iTruckAlertService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iTruckAlertService != null)
      ((javax.xml.rpc.Stub)iTruckAlertService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.tempuri.ITruckAlertService getITruckAlertService() {
    if (iTruckAlertService == null)
      _initITruckAlertServiceProxy();
    return iTruckAlertService;
  }
  
  public java.lang.String getData(java.lang.Integer value) throws java.rmi.RemoteException{
    if (iTruckAlertService == null)
      _initITruckAlertServiceProxy();
    return iTruckAlertService.getData(value);
  }
  
  public org.datacontract.schemas._2004._07.TruckAlertService.CompositeType getDataUsingDataContract(org.datacontract.schemas._2004._07.TruckAlertService.CompositeType composite) throws java.rmi.RemoteException{
    if (iTruckAlertService == null)
      _initITruckAlertServiceProxy();
    return iTruckAlertService.getDataUsingDataContract(composite);
  }
  
  public java.lang.Boolean sendPushNotificationMessage(java.lang.String truckNumber, java.lang.String alertType, java.lang.String message) throws java.rmi.RemoteException{
    if (iTruckAlertService == null)
      _initITruckAlertServiceProxy();
    return iTruckAlertService.sendPushNotificationMessage(truckNumber, alertType, message);
  }
  
  
}