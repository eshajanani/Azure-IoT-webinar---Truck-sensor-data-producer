/**
 * ITruckAlertService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.tempuri;

public interface ITruckAlertService extends java.rmi.Remote {
    public java.lang.String getData(java.lang.Integer value) throws java.rmi.RemoteException;
    public org.datacontract.schemas._2004._07.TruckAlertService.CompositeType getDataUsingDataContract(org.datacontract.schemas._2004._07.TruckAlertService.CompositeType composite) throws java.rmi.RemoteException;
    public java.lang.Boolean sendPushNotificationMessage(java.lang.String truckNumber, java.lang.String alertType, java.lang.String message) throws java.rmi.RemoteException;
}
