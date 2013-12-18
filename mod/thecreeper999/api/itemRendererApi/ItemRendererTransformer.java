package mod.thecreeper999.api.itemRendererApi;


import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM4;
import static org.objectweb.asm.Opcodes.FLOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

import java.util.Iterator;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ItemRendererTransformer implements IClassTransformer {

    private String itemStackClass;
    private String itemRendererClass;
    private String minecraftClass;

    private String itemRendererMinecraftField;

    private String renderItem1stPersonMethodName;
    private String renderItem1stPersonMethodDesc;

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {

        if (transformedName.equals("net.minecraft.client.renderer.ItemRenderer")) {
            System.out.println("ItemRendererAPI - Patching Class ItemRenderer (" + name + ")");


            itemStackClass = /*CreeperTranslator.getMapedClassName("ItemStack")*/ "net/minecraft/item/ItemStack";
            itemRendererClass = /*CreeperTranslator.getMapedClassName("ItemRenderer")*/ "net/minecraft/client/renderer/ItemRenderer";
            minecraftClass = /*CreeperTranslator.getMapedClassName("Minecraft")*/ "net/minecraft/client/Minecraft";
            System.out.println(itemStackClass + " " + itemRendererClass + " " + minecraftClass);

            itemRendererMinecraftField = CreeperTranslator.getMapedFieldName("ItemRenderer", "field_78455_a");

            renderItem1stPersonMethodName = CreeperTranslator.getMapedMethodName("ItemRenderer", "func_78440_a");
            renderItem1stPersonMethodDesc = CreeperTranslator.getMapedMethodDesc("ItemRenderer", "func_78440_a");

            ClassReader cr = new ClassReader(bytes);
            ClassNode cn = new ClassNode(ASM4);

            cr.accept(cn, 0);

            processFields(cn);

            for (Object mnObj : cn.methods) {
                MethodNode mn = (MethodNode)mnObj;
                if (mn.name.equals("renderItemInFirstPerson") &&
                        mn.desc.equals(renderItem1stPersonMethodDesc)) {
                    processRenderItemMethod(mn);
                }
            }


            ClassWriter cw = new ClassWriter(0);
            cn.accept(cw);

            System.out.println("ItemRendererAPI - Patching Class ItemRenderer done");

            return cw.toByteArray();


        } else {
            return bytes;
        }
    }

    private void processRenderItemMethod(MethodNode mn) {

        System.out.println("\tPatching method renderItemInFirstPerson in ItemRenderer");
        InsnList newList = new InsnList();

        Iterator<AbstractInsnNode> it = mn.instructions.iterator();
        while (it.hasNext()) {
            AbstractInsnNode insn = it.next();

            if (insn.getOpcode() == RETURN) {
                newList.add(new VarInsnNode(FLOAD, 1));
                newList.add(new VarInsnNode(ALOAD, 0));
                newList.add(new FieldInsnNode(GETFIELD, itemRendererClass, "mc", "L" + minecraftClass + ";"));
                newList.add(new VarInsnNode(ALOAD, 0));
                newList.add(new VarInsnNode(ALOAD, 0));
                newList.add(new FieldInsnNode(GETFIELD, itemRendererClass, "offHandItemToRender", "L" + itemStackClass + ";"));
                newList.add(new MethodInsnNode(INVOKESTATIC, "mod/thecreeper999/api/itemRendererApi/CreeperUtils", "registerItemRendererEventFirst"
                        , "(FL" + minecraftClass + ";L" + itemRendererClass + ";L" + itemStackClass + ";)V"));
            }

            newList.add(insn);
        }

        mn.instructions = newList;
    }

    private void processFields(ClassNode cn) {
        System.out.println("\tAdding new fields to ItemRenderer");
        cn.fields.add(0, new FieldNode(ACC_PUBLIC, "offHandItemToRender", "L" + itemStackClass + ";", null, null));
        cn.fields.add(1, new FieldNode(ACC_PUBLIC, "equippedItemOffhandSlot", "I", null, 0));
        cn.fields.add(2, new FieldNode(ACC_PUBLIC, "equippedOffHandProgress", "F", null, 0F));
        cn.fields.add(3, new FieldNode(ACC_PUBLIC, "prevEquippedOffHandProgress", "F", null, 0F));
    }
}