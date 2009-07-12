package com.grahamedgecombe.rs2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.grahamedgecombe.rs2.model.Location;
import com.grahamedgecombe.rs2.model.Player;
import com.grahamedgecombe.rs2.model.PlayerDetails;
import com.grahamedgecombe.rs2.util.NameUtils;
import com.grahamedgecombe.util.Streams;

public class GenericWorldLoader implements WorldLoader {

	@Override
	public LoginResult checkLogin(PlayerDetails pd) {
		Player player = null;
		int code = 2;
		File f = new File("data/savedGames/" + NameUtils.formatNameForProtocol(pd.getName()) + ".dat");
		if(f.exists()) {
			try {
				DataInputStream is = new DataInputStream(new FileInputStream(f));
				String name = Streams.readString(is);
				String pass = Streams.readString(is);
				if(!name.equals(NameUtils.formatName(pd.getName()))) {
					code = 3;
				}
				if(!pass.equals(pd.getPassword())) {
					code = 3;
				}
			} catch(IOException ex) {
				code = 11;
			}
		}
		if(code == 2) {
			player = new Player(pd);
		}
		return new LoginResult(code, player);
	}

	@Override
	public boolean savePlayer(Player player) {
		try {
			DataOutputStream os = new DataOutputStream(new FileOutputStream("data/savedGames/" + NameUtils.formatNameForProtocol(player.getName()) + ".dat"));
			Streams.writeString(os, NameUtils.formatName(player.getName()));
			Streams.writeString(os, player.getPassword());
			os.writeByte(player.getRights().toInteger());
			os.writeByte(player.isMembers() ? 1 : 0);
			os.writeShort(player.getLocation().getX());
			os.writeShort(player.getLocation().getY());
			os.writeByte(player.getLocation().getZ());
			os.flush();
			os.close();
			return true;
		} catch(IOException ex) {
			return false;
		}
	}

	@Override
	public boolean loadPlayer(Player player) {
		try {
			DataInputStream is = new DataInputStream(new FileInputStream("data/savedGames/" + NameUtils.formatNameForProtocol(player.getName()) + ".dat"));
			Streams.readString(is);
			Streams.readString(is);
			player.setRights(Player.Rights.getRights(is.readUnsignedByte()));
			player.setMembers(is.readUnsignedByte() == 1 ? true : false);
			player.setLocation(Location.create(is.readUnsignedShort(), is.readUnsignedShort(), is.readUnsignedByte()));
			return true;
		} catch(IOException ex) {
			return false;
		}
	}

}
