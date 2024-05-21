package sn.esmt.gesb.tpo_manager.security;

import java.util.Date;


public record TokenLogin(String token, String role, Date expiryToken) {
}
