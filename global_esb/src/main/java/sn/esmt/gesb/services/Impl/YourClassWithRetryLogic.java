package sn.esmt.gesb.services.Impl;

import org.springframework.context.annotation.Profile;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class YourClassWithRetryLogic {
    @Retryable(retryFor = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 100))
    public void retry(int value) {
        if (value % 2 == 0)
            throw new RuntimeException("Error");
        System.out.println(value);
    }

    @Recover
    public void recover(RuntimeException e, int value) {
        System.out.println("Recover method called for value: " + value);
        // Vous pouvez ajouter une logique de récupération ici
    }
}
