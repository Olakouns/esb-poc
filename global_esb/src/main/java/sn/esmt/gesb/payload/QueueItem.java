package sn.esmt.gesb.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.context.request.async.DeferredResult;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueueItem<T, K> {
    T data;
    DeferredResult<K> kDeferredResult;
}
