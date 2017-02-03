/**
 * TruckAlertServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public class TruckAlertServiceLocator extends org.apache.axis.client.Service implements org.tempuri.TruckAlertService {

    public TruckAlertServiceLocator() {
    }


    public TruckAlertServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TruckAlertServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for BasicHttpsBinding_ITruckAlertService
    private java.lang.String BasicHttpsBinding_ITruckAlertService_address = "https://wcftruckservice20160923021848.azurewebsites.net/TruckAlertService.svc";

    public java.lang.String getBasicHttpsBinding_ITruckAlertServiceAddress() {
        return BasicHttpsBinding_ITruckAlertService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String BasicHttpsBinding_ITruckAlertServiceWSDDServiceName = "BasicHttpsBinding_ITruckAlertService";

    public java.lang.String getBasicHttpsBinding_ITruckAlertServiceWSDDServiceName() {
        return BasicHttpsBinding_ITruckAlertServiceWSDDServiceName;
    }

    public void setBasicHttpsBinding_ITruckAlertServiceWSDDServiceName(java.lang.String name) {
        BasicHttpsBinding_ITruckAlertServiceWSDDServiceName = name;
    }

    public org.tempuri.ITruckAlertService getBasicHttpsBinding_ITruckAlertService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BasicHttpsBinding_ITruckAlertService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBasicHttpsBinding_ITruckAlertService(endpoint);
    }

    public org.tempuri.ITruckAlertService getBasicHttpsBinding_ITruckAlertService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.tempuri.BasicHttpsBinding_ITruckAlertServiceStub _stub = new org.tempuri.BasicHttpsBinding_ITruckAlertServiceStub(portAddress, this);
            _stub.setPortName(getBasicHttpsBinding_ITruckAlertServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBasicHttpsBinding_ITruckAlertServiceEndpointAddress(java.lang.String address) {
        BasicHttpsBinding_ITruckAlertService_address = address;
    }


    // Use to get a proxy class for BasicHttpBinding_ITruckAlertService
    private java.lang.String BasicHttpBinding_ITruckAlertService_address = "http://wcftruckservice20160923021848.azurewebsites.net/TruckAlertService.svc";

    public java.lang.String getBasicHttpBinding_ITruckAlertServiceAddress() {
        return BasicHttpBinding_ITruckAlertService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String BasicHttpBinding_ITruckAlertServiceWSDDServiceName = "BasicHttpBinding_ITruckAlertService";

    public java.lang.String getBasicHttpBinding_ITruckAlertServiceWSDDServiceName() {
        return BasicHttpBinding_ITruckAlertServiceWSDDServiceName;
    }

    public void setBasicHttpBinding_ITruckAlertServiceWSDDServiceName(java.lang.String name) {
        BasicHttpBinding_ITruckAlertServiceWSDDServiceName = name;
    }

    public org.tempuri.ITruckAlertService getBasicHttpBinding_ITruckAlertService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BasicHttpBinding_ITruckAlertService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBasicHttpBinding_ITruckAlertService(endpoint);
    }

    public org.tempuri.ITruckAlertService getBasicHttpBinding_ITruckAlertService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.tempuri.BasicHttpBinding_ITruckAlertServiceStub _stub = new org.tempuri.BasicHttpBinding_ITruckAlertServiceStub(portAddress, this);
            _stub.setPortName(getBasicHttpBinding_ITruckAlertServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBasicHttpBinding_ITruckAlertServiceEndpointAddress(java.lang.String address) {
        BasicHttpBinding_ITruckAlertService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.tempuri.ITruckAlertService.class.isAssignableFrom(serviceEndpointInterface)) {
                org.tempuri.BasicHttpsBinding_ITruckAlertServiceStub _stub = new org.tempuri.BasicHttpsBinding_ITruckAlertServiceStub(new java.net.URL(BasicHttpsBinding_ITruckAlertService_address), this);
                _stub.setPortName(getBasicHttpsBinding_ITruckAlertServiceWSDDServiceName());
                return _stub;
            }
            if (org.tempuri.ITruckAlertService.class.isAssignableFrom(serviceEndpointInterface)) {
                org.tempuri.BasicHttpBinding_ITruckAlertServiceStub _stub = new org.tempuri.BasicHttpBinding_ITruckAlertServiceStub(new java.net.URL(BasicHttpBinding_ITruckAlertService_address), this);
                _stub.setPortName(getBasicHttpBinding_ITruckAlertServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("BasicHttpsBinding_ITruckAlertService".equals(inputPortName)) {
            return getBasicHttpsBinding_ITruckAlertService();
        }
        else if ("BasicHttpBinding_ITruckAlertService".equals(inputPortName)) {
            return getBasicHttpBinding_ITruckAlertService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://tempuri.org/", "TruckAlertService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "BasicHttpsBinding_ITruckAlertService"));
            ports.add(new javax.xml.namespace.QName("http://tempuri.org/", "BasicHttpBinding_ITruckAlertService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("BasicHttpsBinding_ITruckAlertService".equals(portName)) {
            setBasicHttpsBinding_ITruckAlertServiceEndpointAddress(address);
        }
        else 
if ("BasicHttpBinding_ITruckAlertService".equals(portName)) {
            setBasicHttpBinding_ITruckAlertServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
