package share.connection.socket;

import java.io.IOException;
import java.io.ObjectOutputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

// error in mockito i can't test more
public class TestSocketOutput {

	private ObjectOutputStream out;
	private SocketOutput socketo;

	@Before
	public void configure() throws ClassNotFoundException, IOException {

		this.out = Mockito.mock(ObjectOutputStream.class);
		// Mockito.when(this.out.writeObject("test")).

		this.socketo = new SocketOutput();
		// Whitebox.setInternalState(this.socketInput, "socket", this.socket);
		Whitebox.setInternalState(this.socketo, "oos", this.out);
	}

	@Test
	public void sendAndClose() {
		// this.socketo.sendObj("test");
		this.socketo.closeOutput();
	}
}
