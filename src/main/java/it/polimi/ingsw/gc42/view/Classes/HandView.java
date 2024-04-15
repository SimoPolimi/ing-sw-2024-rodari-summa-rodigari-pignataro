package it.polimi.ingsw.gc42.view.Classes;

import it.polimi.ingsw.gc42.view.GUIController;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class HandView {
    private HandCardView handCardView1;
    private HandCardView handCardView2;
    private HandCardView handCardView3;

    private ImageView KBNavHint;
    private Text textNav;
    private ImageView KBCollapseHint;
    private Text textCollapse;
    GUIController controller;
    private boolean isHidden;

    public HandView(HandCardView handCardView1, HandCardView handCardView2, HandCardView handCardView3,
                    ImageView KBNavHint, Text textNav, ImageView KBCollapseHint, Text textCollapse,
                    GUIController controller) {
        this.handCardView1 = handCardView1;
        this.handCardView2 = handCardView2;
        this.handCardView3 = handCardView3;
        this.KBNavHint = KBNavHint;
        this.textNav = textNav;
        this.KBCollapseHint = KBCollapseHint;
        this.textCollapse = textCollapse;
        this.controller = controller;

    }

    public boolean isHidden() {
        return isHidden;
    }

    public HandCardView getHandCardView(int number) {
        switch (number) {
            case 1 -> {
                return handCardView1;
            }
            case 2 -> {
                return handCardView2;
            }
            case 3 -> {
                return handCardView3;
            }
            default -> {
                return null;
            }
        }
    }

    public void hide() {
        controller.deselectAllCards();
        isHidden = true;
        KBNavHint.setVisible(false);
        textNav.setVisible(false);
        textCollapse.setText("My Cards");
        controller.blockInput();

        TranslateTransition t1 = new TranslateTransition(Duration.millis(350), textCollapse);
        t1.setByY(285);
        t1.play();

        TranslateTransition t2 = new TranslateTransition(Duration.millis(350), KBCollapseHint);
        t2.setByY(285);
        t2.play();

        handCardView1.hide(1, controller);
        handCardView2.hide(2, controller);
        handCardView3.hide(3, controller);
    }

    public void show() {
        isHidden = false;
        KBNavHint.setVisible(true);
        textNav.setVisible(true);
        textCollapse.setText("Collapse");
        controller.blockInput();

        TranslateTransition t1 = new TranslateTransition(Duration.millis(350), textCollapse);
        t1.setByY(-285);
        t1.play();

        TranslateTransition t2 = new TranslateTransition(Duration.millis(350), KBCollapseHint);
        t2.setByY(-285);
        t2.play();

        handCardView1.show(1, controller);
        handCardView2.show(2, controller);
        handCardView3.show(3, controller);
    }

    public void refresh(Runnable runnable) {
        TranslateTransition t1 = new TranslateTransition(Duration.millis(350), handCardView1.getImageView());
        TranslateTransition t2 = new TranslateTransition(Duration.millis(350), handCardView2.getImageView());
        TranslateTransition t3 = new TranslateTransition(Duration.millis(350), handCardView3.getImageView());
        int distance;
        if (isHidden) {
            distance = -100;
        } else {
            distance = -300;
        }
        t1.setByX(distance);
        t2.setByX(distance);
        t3.setByX(distance);

        t1.setOnFinished((e) -> {
            runnable.run();
            showAfterRefresh();
        });
        t1.play();
        t2.play();
        t3.play();
    }

    private void showAfterRefresh() {
        TranslateTransition t1 = new TranslateTransition(Duration.millis(350), handCardView1.getImageView());
        TranslateTransition t2 = new TranslateTransition(Duration.millis(350), handCardView2.getImageView());
        TranslateTransition t3 = new TranslateTransition(Duration.millis(350), handCardView3.getImageView());
        int distance;
        if (isHidden) {
            distance = 100;
        } else {
            distance = 300;
        }
        t1.setByX(distance);
        t2.setByX(distance);
        t3.setByX(distance);
        t1.play();
        t2.play();
        t3.play();
    }
}
