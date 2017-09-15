package cn.vko.ring.home.model;

public class Recommond {
	private int img;
	private String title;

	public Recommond(int img, String title) {
		super();
		this.img = img;
		this.title = title;
	}

	public int getImg() {
		return img;
	}

	public void setImg(int img) {
		this.img = img;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}


