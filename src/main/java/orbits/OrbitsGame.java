package orbits;

import gamelauncher.engine.event.EventHandler;
import gamelauncher.engine.event.events.LauncherInitializedEvent;
import gamelauncher.engine.event.events.gui.GuiOpenEvent;
import gamelauncher.engine.game.Game;
import gamelauncher.engine.gui.GuiDistribution;
import gamelauncher.engine.gui.guis.ButtonGui;
import gamelauncher.engine.gui.guis.MainScreenGui;
import gamelauncher.engine.network.packet.PacketNotRegisteredException;
import gamelauncher.engine.render.Framebuffer;
import gamelauncher.engine.util.GameException;
import orbits.data.LevelStorage;
import orbits.gui.CustomGui;
import orbits.gui.OrbitsMainScreenGui;
import orbits.lobby.Lobby;
import orbits.network.PacketHandlers;

public class OrbitsGame extends Game {
    private final Orbits orbits;
    private final PacketHandlers packetHandlers = new PacketHandlers(this);
    private Lobby lobby;
    private volatile LevelStorage levelStorage;

    public OrbitsGame(Orbits orbits) throws GameException {
        super(orbits, "orbits");
        this.orbits = orbits;
        levelStorage = new LevelStorage(this);
        launcher().guiManager().registerGuiCreator(GuiDistribution.DEFAULT, ButtonGui.class, CustomGui.class);
    }

    @Override
    protected void launch0(Framebuffer framebuffer) throws GameException {
//        packetHandlers().registerHandlers();
    }

    @Override
    protected void close0() throws PacketNotRegisteredException {
//        packetHandlers().unregisterHandlers();
    }

    @EventHandler
    private void handle(GuiOpenEvent event) throws GameException {
        if (event.gui() instanceof MainScreenGui) {
            event.gui(new OrbitsMainScreenGui(this));
        }
    }

    @EventHandler
    private void handle(LauncherInitializedEvent event) throws GameException {
        launch(event.launcher().frame().framebuffer());
    }

    public LevelStorage levelStorage() {
        return levelStorage;
    }

    public PacketHandlers packetHandlers() {
        return packetHandlers;
    }

    public Lobby currentLobby() {
        if (Thread.currentThread() != launcher().gameThread())
            throw new RuntimeException("May only access this from GameThread");
        return lobby;
    }

    public void currentLobby(Lobby lobby) {
        if (Thread.currentThread() != launcher().gameThread())
            throw new RuntimeException("May only access this from GameThread");
        this.lobby = lobby;
    }
}
