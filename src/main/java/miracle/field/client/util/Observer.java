package miracle.field.client.util;

import miracle.field.shared.packet.Packet;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class Observer {

    private Map<String, List<Waiter>> waiterList;

    public Observer() {
        this.waiterList = new HashMap<>();
    }

    public void addWaiter(String s, Waiter waiter) {
        if (waiterList.get(s) != null)
            waiterList.get(s).add(waiter);
        else
            waiterList.put(s, new LinkedList<>(){
                {
                    add(waiter);
                }
            });
    }

    public void removeWaiter(String s, Waiter waiter) {
        waiterList.get(s).remove(waiter);
    }

    public void notifyClients(Packet packet) throws IOException {
        System.out.println("Notifying windows about " + packet.getType());
        List<Waiter> waiters = waiterList.get(packet.getType());
        for (Waiter w : waiters)
            w.getNotify(packet);
    }

}
