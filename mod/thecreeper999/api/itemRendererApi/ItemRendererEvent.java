package mod.thecreeper999.api.itemRendererApi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.Event;

public class ItemRendererEvent extends Event{

	public float partialTick;
	public Minecraft mc;
	public ItemRenderer renderer;
	public ItemStack itemToRender;

	public static class RenderInFirstPerson extends ItemRendererEvent
	{
		public RenderInFirstPerson(float partialTick, Minecraft mc, ItemRenderer renderer, ItemStack itemToRender){
			super();
			this.partialTick = partialTick;
			this.mc = mc;
			this.renderer = renderer;
			this.itemToRender = itemToRender;
		}
	}

}
