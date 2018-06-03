package share.connection.socket;

public class TestSocketInput {
	// i can't test it , error with Mockito.when(ois.readObject())

	// private Socket socket;
	// private ObjectInputStream ois;
	// private SocketInput socketInput;
	//
	// @Before
	// public void configure() throws ClassNotFoundException, IOException {
	//
	// InputStream in = Mockito.mock(InputStream.class);
	// ObjectInputStream ois = new ObjectInputStream(in);
	// // this.socket = Mockito.mock(Socket.class);
	// // Mockito.when(this.socket.isClosed()).thenReturn(false);
	//
	// this.ois = Mockito.mock(ObjectInputStream.class);
	// // Mockito.when(this.ois.readObject()).thenThrow(new IOException());
	//
	// this.socketInput = new SocketInput();
	// // Whitebox.setInternalState(this.socketInput, "socket", this.socket);
	// Whitebox.setInternalState(this.socketInput, "ois", ois);
	// }
	//
	// @Test
	// public void sdfg() {
	// assertFalse(this.socketInput.receiveObj() == "test");
	// assertFalse(this.socketInput.receiveString() == "test");
	// }
}
