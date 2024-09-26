package developx.book.parallel.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LongExecutionEvent {

    public static void main(String[] args) {

        ExecutorService backGroundExec = Executors.newCachedThreadPool();
        JButton jButton = new JButton();
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jButton.setEnabled(false);
                backGroundExec.execute(() -> {
                    try{
                        doRunEvent();
                    } finally {
                        GuiExecutor.getInstance().execute(() -> {
                            jButton.setEnabled(true);
                        });
                    }
                });
            }
        });
    }

    private static void doRunEvent() {
        // do something
    }
}
