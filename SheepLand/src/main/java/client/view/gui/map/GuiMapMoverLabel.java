package client.view.gui.map;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * class to handle the timer of moving of label
 * 
 * @author mirko conti
 * 
 */
public class GuiMapMoverLabel {

	// distance for each "move" of timer
	private static final int DIST_MOVE = 10;
	private static final int TIME_FOR_MOVE = 20;
	private static final int TIME_INITIAL_DELAY = 100;
	private int distMove;

	private Point startPos, finalPos;
	private boolean hide;

	private Timer timerMoveLabel;
	private GuiMapLabel labelToMove;
	// jframe to refresh
	private JFrame jframe;

	private int ratioMove;
	private int numMoveToDoWithRoundHigh;
	private int numMoveToDo;
	private int signumRatio, signumPosition = 1;
	// ratio yx > 1
	private boolean useRatioXY;
	// to avoid the zoom and rotate during the motion of a label
	private GuiMap guiMap;

	/**
	 * 
	 * @param labelToMove
	 * @param jframe
	 *            to refresh
	 */
	public GuiMapMoverLabel(JFrame jframe, GuiMap guiMap) {
		this.jframe = jframe;
		this.guiMap = guiMap;
		this.timerMoveLabel = new Timer(TIME_FOR_MOVE,
				new eventTimerMoveLabel());
		this.timerMoveLabel.setInitialDelay(TIME_INITIAL_DELAY);
	}

	/**
	 * must set before a new move
	 * 
	 * @param labelToMove
	 *            the label you want to move
	 * @param startPosX
	 *            the start x position of move, maybe it's just the actual
	 *            position of label
	 * @param startPosY
	 *            the start y position of move, maybe it's just the actual
	 *            position of label
	 * @param hide
	 *            set true if you want show the label , move it and then hide it
	 *            again
	 */
	public void setLabelToMove(GuiMapLabel labelToMove, Point startPos,
			boolean hide) {
		this.labelToMove = labelToMove;
		this.startPos = startPos;
		this.hide = hide;
	}

	/**
	 * must set before a new move
	 * 
	 * @param labelToMov
	 *            the label to move , it will starts from the original position
	 * 
	 */
	public void setLabelToMove(GuiMapLabel labelToMov) {
		this.labelToMove = labelToMov;
	}

	/**
	 * start the timer (thread) and call setDestinationLabel with the point of
	 * the centre of position final (usually a label)
	 * 
	 * @param finalPos
	 *            is the center of final label
	 */
	public void startTimer(Point finalPos) {
		setDestinationLabel(finalPos);
		if (this.timerMoveLabel.isRunning()) {
			this.timerMoveLabel.stop();
		}
		this.timerMoveLabel.start();
	}

	/**
	 * set different distant move that change with zoom if label is zoomed the
	 * distMove have to be bigger
	 */
	private void fixVelocity() {
		if (this.labelToMove.getZoom() == 0) {
			this.distMove = DIST_MOVE;
		} else {
			this.distMove = DIST_MOVE * 2;
		}
	}

	/**
	 * get the final position and calculate the num of move to do, the error
	 * with a straight line and generate the numMoveTo-DoWithRoundHigh the
	 * remain of move have to do with RoundLow
	 * 
	 * @param finalPos
	 *            final destination of label to move
	 */
	private void setDestinationLabel(Point finalPosition) {

		this.finalPos = this.labelToMove.fixPosition(finalPosition, false);
		// set initial position (usually dont move the label)
		if (this.startPos != null) {
			this.labelToMove.fixPosition(this.startPos, true);
		}
		// the sheep_move is hidden initially
		if (this.hide) {
			this.labelToMove.getLabel().setVisible(true);
		}
		fixVelocity();

		float deltaX = this.finalPos.x - this.labelToMove.getLabel().getX();
		float deltaY = this.finalPos.y - this.labelToMove.getLabel().getY();
		float ratio = deltaY / deltaX;

		// math with the four quadrants of XY
		if (Math.abs(ratio) > 1) {
			ratio = 1 / ratio;
			this.useRatioXY = true;
			this.signumPosition = (int) Math.signum(deltaY);
		} else {
			// ratio YX
			this.useRatioXY = false;
			this.signumPosition = (int) Math.signum(deltaX);
		}
		// signumRation positive for 1° & 3° quadrant
		this.signumRatio = (int) Math.signum(ratio);
		this.ratioMove = (int) (ratio * this.distMove);
		// calc max move to do
		this.numMoveToDo = Math.round(Math.max(Math.abs(deltaX),
				Math.abs(deltaY))
				/ this.distMove);
		// precision
		float percError = ratio * this.distMove - this.ratioMove;
		this.numMoveToDoWithRoundHigh = Math.round(Math.abs(percError
				* this.numMoveToDo));
	}

	/**
	 * magic math that tell if the actual position if the final one
	 * 
	 * @return the answer
	 */
	private boolean moveLabelRatioYX() {
		int precision;
		if (this.numMoveToDoWithRoundHigh != 0) {
			precision = (int) (this.signumRatio * 1);
			this.numMoveToDoWithRoundHigh -= 1;
		} else {
			precision = 0;
		}

		if (this.numMoveToDo > 0) {
			if (!this.useRatioXY) {
				moveLabel(this.signumPosition * this.distMove,
						this.signumPosition * (this.ratioMove + precision));
			} else {
				moveLabel(this.signumPosition * (this.ratioMove + precision),
						this.signumPosition * this.distMove);
			}
			this.numMoveToDo -= 1;
		} else {
			return false;
		}
		return true;
	}

	/**
	 * move the label of x and y pixel among the old position
	 * 
	 * @param moreX
	 * @param moreY
	 */
	private void moveLabel(int moreX, int moreY) {
		this.labelToMove.getLabel().setLocation(
				this.labelToMove.getLabel().getX() + moreX,
				this.labelToMove.getLabel().getY() + moreY);
	}

	/**
	 * event called every clock of timer
	 * 
	 * @author mirko conti
	 * 
	 */
	private class eventTimerMoveLabel implements ActionListener {

		/**
		 * check have to move the label and call the method that move it finally
		 * stop the timer and, if have to hide the label, hide it
		 */
		public void actionPerformed(ActionEvent e) {
			GuiMapMoverLabel.this.guiMap.setMoving(true);
			if (!moveLabelRatioYX()) {
				GuiMapMoverLabel.this.timerMoveLabel.stop();
				if (GuiMapMoverLabel.this.hide) {
					GuiMapMoverLabel.this.labelToMove.getLabel().setVisible(
							false);
				} else {
					// FIX final position
					GuiMapMoverLabel.this.labelToMove.getLabel().setLocation(
							GuiMapMoverLabel.this.finalPos);
				}
				GuiMapMoverLabel.this.guiMap.setMoving(false);
			}
			GuiMapMoverLabel.this.jframe.repaint();
		}
	}
}
