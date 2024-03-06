package entities;

import java.util.Objects;

public class CoursFavoris {
    private int courFavoris_ID;
    private Cour cour;
    private User user;

    public CoursFavoris()
    {

    }
    public CoursFavoris(int courfav, User u, Cour c)
    {
        this.courFavoris_ID = courfav;
        this.user = u;
        this.cour = c;
    }
    public int getCourFavoris_ID() {
        return courFavoris_ID;
    }

    public void setCourFavoris_ID(int courFavoris_ID) {
        this.courFavoris_ID = courFavoris_ID;
    }

    public Cour getCour() {
        return cour;
    }

    public void setCour(Cour cour) {
        this.cour = cour;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CoursFavoris{" +
                "courFavoris_ID=" + courFavoris_ID +
                ", cour=" + cour +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoursFavoris that = (CoursFavoris) o;
        return courFavoris_ID == that.courFavoris_ID && Objects.equals(cour, that.cour) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courFavoris_ID, cour, user);
    }
}
