package nl.imine.api.holotag;

import java.util.Arrays;

import org.bukkit.Location;

public abstract class Tag implements ITag {

	private transient ILine[] lines = new ILine[0];

	private Location location;
	private double lineDistance;
	private boolean visible;

	Tag(Location location) {
		this(location, 0.30);
	}

	Tag(Location location, double lineDistance) {
		this.location = location;
		this.lineDistance = lineDistance;
	}

	@Override
	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public void setLineDistance(double lineDistance) {
		this.lineDistance = lineDistance;
	}

	@Override
	public double getLineDistance() {
		return lineDistance;
	}

	@Override
	@Deprecated
	public void setLine(String line, int row) {
		setLine(row, line);
	}

	@Override
	public void setLine(int row, String line) {
		if (row >= getLines().length) {
			setLines(Arrays.copyOf(getLines(), row + 1));
		}
		ILine theLine = getLines()[row];
		if (theLine == null) {
			getLines()[row] = theLine = new Line(line, location.clone().add(0, (getLineDistance() * row), 0),
					isVisible());
		}
		theLine.setLabel(line);
	}

	@Override
	public void addLine(String line) {
		setLines(Arrays.copyOf(getLines(), getLines().length + 1));
		getLines()[getLines().length - 1] = new Line(line,
				location.clone().add(0, -(getLineDistance() * getLines().length - 1), 0), isVisible());
	}

	@Override
	public ILine getLine(int row) {
		if (row < getLines().length) {
			if (getLines()[row] == null) {
				getLines()[row] = new Line(" ", location.clone().add(0, -(getLineDistance() * row), 0), isVisible());
			}
			return getLines()[row];
		}
		return null;
	}

	@Override
	public void removeLine(int row) {
		if (row < getLines().length) {
			if (getLines()[row] != null) {
				getLines()[row].removeArmorStand();
				getLines()[row] = null;
			}
		}
	}

	@Override
	public void removeLine(ILine line) {
		for (int i = 0; i < getLines().length; i++) {
			if (getLines()[i].equals(line)) {
				removeLine(i);
			}
		}
	}

	protected void setLines(ILine[] lines) {
		this.lines = lines;
	}

	@Override
	public ILine[] getLines() {
		if (lines == null) {
			setLines(new ILine[0]);
		}
		return lines;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
		Arrays.asList(getLines()).stream().filter(l -> l != null).forEach(l -> l.setVisible(visible, true));
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void remove() {
		for (int i = 0; i < getLines().length; i++) {
			removeLine(i);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ITag) {
			ITag tag = (ITag) obj;
			return tag.getLines().equals(getLines()) && tag.getLocation().equals(getLocation());
		}
		return super.equals(obj);
	}
}
