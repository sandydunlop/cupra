package io.github.sandydunlop.cupra.platform.desktop;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import io.github.sandydunlop.cupra.common.CupraScreen;
import io.github.sandydunlop.cupra.common.events.CMouseEvent;
import io.github.sandydunlop.cupra.common.CupraException;
import io.github.sandydunlop.cupra.common.fonts.FontSpec;
import io.github.sandydunlop.cupra.common.util.DepthLimit;
import io.github.sandydunlop.cupra.common.widgets.CWidget;
import io.github.sandydunlop.cupra.platform.PlatformServices;


public class CupraWindow  extends JPanel implements WindowStateListener, MouseListener, MouseMotionListener, MouseWheelListener {
    private transient JFrame frame = null;
    private transient AbstractMouseInputHandler inputHandler = null;
    private transient AwtRenderer renderer = null;
    private transient CupraScreen screen = null;
    private transient CWidget draggingWidget = null;
    private transient FontSpec fontOptions = new FontSpec();
    private Cursor arrowCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    private Cursor iBeamCursor = new Cursor(Cursor.TEXT_CURSOR);
    private Cursor pointingHandCursor = new Cursor(Cursor.HAND_CURSOR);
    private Cursor currentCursor = arrowCursor;
    private int backgroundColor;
    private int windowWidth;
    private int windowHeight;
    private boolean designMode = false;
    protected double dragFromX = -1;
    protected double dragFromY = -1;


    public CupraWindow(CupraScreen screen) {
        super();
        windowWidth = screen.getSuggestedWidth() > 0 ? screen.getSuggestedWidth() : 800;
        windowHeight = screen.getSuggestedHeight() > 0 ? screen.getSuggestedHeight() : 600;

        this.screen = screen;

        this.setSize(windowWidth, windowHeight);
        fontOptions.setName("Rubik");
        fontOptions.setColor(CWidget.getPalette().REGULAR_TEXT);

        DesktopServices.getInstance();

        frame = new JFrame(screen.getTitle());
        //initMenus(frame);

        frame.setSize(windowWidth, windowHeight + 40);
        frame.getContentPane().setBackground(AwtRenderer.colorFromInt(CWidget.getPalette().REGULAR_BACKGROUND));
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(windowWidth,windowHeight));
        this.setBackgroundColor(CWidget.getPalette().REGULAR_BACKGROUND);
        frame.add(this);
        screen.setWidth(windowWidth);
        screen.setHeight(windowHeight);
        FrameMonitor.registerFrame(frame, screen, screen.getClass().getName(),
                0, 0, 800, 600);

        frame.setVisible(true);
        // frame.addKeyListener(this);
        this.requestFocusInWindow();

        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        // DesktopServices.getInstance().getWindowManager().
        //onWindowFocused(this);

        screen.init();
    }    


    // public void initMenus(JFrame frame) {
    //     JMenuBar menuBar = new JMenuBar();
    //     windowMenu = new JMenu("Window");
    //     JMenu windowMenu = DesktopServices.getInstance().getWindowMenu();
    //     menuBar.add(windowMenu);
    //     frame.setJMenuBar(menuBar);
    // }


    public JFrame getFrame() {
        return frame;
    }


    public CupraScreen getScreen() {
        return screen;
    }


    public void setWindowSize(int width, int height) {
        frame.setSize(width, height);
        this.setSize(width, height);
    }


    public void setInputHandler(AbstractMouseInputHandler inputHandler) {
        this.inputHandler = inputHandler;
        if (inputHandler != null) {
            setDesignMode(true);
        }else{
            setDesignMode(false);
        }
    }


    public void setDesignMode(boolean designMode) {
        this.designMode = designMode;
        if (designMode) {
            setBackgroundColor(0xFF094E4D);
            //0xFF280823); //0xFF281801); #280823. 0xFF094E4D
        } else {
            setBackgroundColor(this.backgroundColor);
        }
    }


    public boolean getDesignMode() {
        return designMode;
    }


    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    swingStuff(color);
                }
            });
        t.start();
    }


    public void swingStuff(int color) {
        Color awtColor = AwtRenderer.colorFromInt(color);
        CupraWindow me = this;
        try{
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    me.setBackground(awtColor);
                }
            });
        }catch(InvocationTargetException | InterruptedException e){
            e.printStackTrace();
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
    }


    public void windowStateChanged(WindowEvent e) {
        if (e.getNewState() == WindowEvent.WINDOW_GAINED_FOCUS) {
            screen.getApp().setScreen(screen);
        }
    }

    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintComponent(g, null, 0, 0, 0);
    }


    protected void paintComponent(Graphics g, CWidget widget, int mouseX, int mouseY, float delta) {
        Graphics2D g2d = (Graphics2D) g;
        if (renderer == null){
            renderer = new AwtRenderer(g2d);
            renderer.setFont(fontOptions);
        }else{
            renderer = new AwtRenderer(g2d);
            renderer.setFont(fontOptions);
        }
        try {
            if (widget != null) {
                widget.render(renderer, mouseX, mouseY, delta);
            }else if (screen != null){
                if (!screen.isInitialized()){
                    screen.init();
                }
                screen.render(renderer, mouseX, mouseY, delta);
            }
        } catch (CupraException e) {
            e.printStackTrace();
        }
        if (designMode) {
            drawRulers();
        }
    }


    private void drawRulers() {
        int color = 0xFFF26C0B;
        color = 0xFFF79B2B | 0xFFBBBBBB;
        for (int i = 0; i < screen.getWidth(); i+=20) {
            int j = (i%100)==0 ? 10 : 5;
            renderer.drawVerticalLine(i, 0, j, color);
        }
        for (int i = 0; i < screen.getHeight(); i+=20) {
            int j = (i%100)==0 ? 10 : 5;
            renderer.drawHorizontalLine(0, j, i, color);
        }
    }


    public void renderWidget(CWidget widget) {
        if (widget != null) {
            this.repaint(widget.getCalculatedX(),
                    widget.getCalculatedY(),
                    widget.getCalculatedX() + widget.getWidth(),
                    widget.getCalculatedY() + widget.getHeight());
            return;
        }
        this.repaint();
    }


    public void render() {
        this.repaint();
    }


    public void setWindowTitle(String title){
        frame.setTitle(title);
    }


    //
    // === Mouse ===
    //


    private CMouseEvent cupraMouseEvent(MouseEvent e) {
        CMouseEvent event = new CMouseEvent()
                .setX(e.getX())
                .setY(e.getY())
                .setButton(e.getButton())
                .setClickCount(e.getClickCount())
                .setModifiers(e.getModifiersEx());
        if (e instanceof MouseWheelEvent wheel) {
            if (event.isShiftPressed()) {
                event.setHorizontalAmount(wheel.getPreciseWheelRotation());
            } else {
                double scrollAmount = PlatformServices.getInstance().getInvertMouseScrolling() ? 
                        -wheel.getPreciseWheelRotation() : wheel.getPreciseWheelRotation();
                event.setVerticalAmount(scrollAmount);
            }
        }
        return event;
    }


    public void mouseClicked(MouseEvent e) {
        CMouseEvent mouse = cupraMouseEvent(e);
        if (inputHandler == null) {
            CWidget widget = screen.hoveredWidget(mouse, DepthLimit.COMPONENT);
            if (widget == null) {
                return;
            }else{
                if (e.getClickCount() == 1) {
                    widget.mouseClicked(mouse);
                } else if (e.getClickCount() == 2) {
                    widget.mouseDoubleClicked(mouse);
                } else if (e.getClickCount() == 3) {
                    widget.mouseTripleClicked(mouse);
                }
            }
            setMousePointerForWidget(widget);
        }else{
            CWidget widget = screen.hoveredWidget(mouse, DepthLimit.WIDGET);
            inputHandler.mouseClicked(widget, mouse);
        }
        repaint();
	}


    public void mousePressed(MouseEvent e) {
        CMouseEvent mouse = cupraMouseEvent(e);
        if (inputHandler == null) {
            CWidget widget = screen.hoveredWidget(mouse, DepthLimit.COMPONENT);
            if (widget != null) {
                if (!screen.isMouseOverOverlaid(mouse)) {
                    screen.hideOverlaidWidget();
                }
                if (widget.isFocusable()) {
                    screen.setFocus(widget);
                }
                widget.mousePressed(mouse);
                draggingWidget = widget;
                dragFromX = mouse.getX();
                dragFromY = mouse.getY();
                setMousePointerForWidget(widget);
            } else {
                screen.hideOverlaidWidget();
            }
        }else{
            CWidget widget = screen.hoveredWidget(mouse, DepthLimit.WIDGET);
            inputHandler.mousePressed(widget, mouse);
        }
        repaint();
    }


    public void mouseReleased(MouseEvent e) {
        draggingWidget = null;
    }


    public void mouseEntered(MouseEvent e) {
    }


    public void mouseExited(MouseEvent e) {
    }


    public void mouseWheelMoved(MouseWheelEvent e) {
        CMouseEvent mouse = cupraMouseEvent(e);
        if (inputHandler == null) {
            CWidget widget = screen.hoveredWidget(mouse, DepthLimit.SCROLLABLE);
            if (widget != null) {
                widget.mouseScrolled(mouse);
                repaint();
            }
        }else{
            CWidget widget = screen.hoveredWidget(mouse, DepthLimit.WIDGET);
            inputHandler.mouseScrolled(widget, mouse);
        }
    }


    public void mouseDragged(MouseEvent e) {
        CMouseEvent mouse = cupraMouseEvent(e);
        if (draggingWidget == null) {
			return;
		}

        double deltaX = mouse.getX() - dragFromX;
        double deltaY = mouse.getY() - dragFromY;
        mouse.setDeltaX(deltaX);
        mouse.setDeltaY(deltaY);

        if (draggingWidget.mouseDragged(mouse)) {
            repaint();
        }
        dragFromX = mouse.getX();
        dragFromY = mouse.getY();
    }


    // private CWidget mouseIsOver = null;
    public void mouseMoved(MouseEvent e) {
        CMouseEvent mouse = cupraMouseEvent(e);
        if (inputHandler == null) {
            CWidget widget = screen.hoveredWidget(mouse, DepthLimit.COMPONENT);
            if (widget != null) {
                setMousePointerForWidget(widget);
                // if (widget.hasMosueOverEffects()) {
                //     renderWidget(widget, e.getX(), e.getY(), 0);
                //     mouseIsOver = widget;
                // }
            }else{
                currentCursor = arrowCursor;
                setCursor(currentCursor);
                // if (mouseIsOver != null) {
                //     renderWidget(widget, e.getX(), e.getY(), 0);
                //     mouseIsOver = null;
                // }
            }
        }else{
            CWidget widget = screen.hoveredWidget(mouse, DepthLimit.COMPONENT);
            inputHandler.mouseMoved(widget, mouse);
        }
    }


    private void setMousePointerForWidget(CWidget widget) {
        Cursor updatedCursor = currentCursor;
        switch (widget.getMousePointer()) {
            case POINTING_HAND:
                updatedCursor = pointingHandCursor;
                break;
            case I_BEAM:
                updatedCursor = iBeamCursor;
                break;
            default:
                updatedCursor = arrowCursor;
                break;
        }
        if (updatedCursor != currentCursor) {
            currentCursor = updatedCursor;
            setCursor(currentCursor);
        }
    }
}
