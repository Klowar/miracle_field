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

    public void addWaiter(String name, Waiter waiter) {
        if (waiterList.get(name) != null)
            waiterList.get(name).add(waiter);
        else
            waiterList.put(name, new LinkedList<>(){
                {
                    add(waiter);
                }
            });
    }

    public void removeWaiter(String s, Waiter waiter) {
        waiterList.get(s).remove(waiter);
    }

    public void notifyClients(Packet packet)  {
        System.out.println("Notifying windows about " + packet.getType());
        List<Waiter> waiters = waiterList.get(packet.getType());
        for (Waiter w : waiters)
            w.getNotify(packet);
    }

}
