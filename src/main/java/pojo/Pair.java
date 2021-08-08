package pojo;

import java.util.Set;

public class Pair {

    private Set<User> users;
    private Set<Ticket> tickets;

    public Pair(Set<User> users, Set<Ticket> tickets) {
        this.users = users;
        this.tickets = tickets;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }
}
