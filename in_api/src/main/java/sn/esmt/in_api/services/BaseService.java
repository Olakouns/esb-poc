package sn.esmt.in_api.services;

import sn.esmt.in_api.models.SubscriberUser;
import sn.esmt.in_api.soam.ApiResponse;
import sn.esmt.in_api.soam.NewConnectionRequest;
import sn.esmt.in_api.soam.RechargingRequest;
import sn.esmt.in_api.soam.TerminationRequest;

public interface BaseService {
    ApiResponse newConnection(NewConnectionRequest newConnectionRequest);

    ApiResponse termination(TerminationRequest terminationRequest);

    ApiResponse query(String number);

    ApiResponse recharging(RechargingRequest rechargingRequest);
}
