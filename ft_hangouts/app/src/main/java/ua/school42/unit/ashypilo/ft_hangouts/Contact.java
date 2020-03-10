package ua.school42.unit.ashypilo.ft_hangouts;

public class Contact {
    private int Image;
    private String Text;

    public Contact(int image, String text) {
        this.Image = image;
        this.Text = text;
    }

    public int getImage() {
        return Image;
    }

    public String getText() {
        return Text;
    }

    public void setImage(int image) {
        Image = image;
    }

    public void setText(String text) {
        Text = text;
    }

    @Override
    public String toString() {
        return (getImage() + getText());
    }
}
