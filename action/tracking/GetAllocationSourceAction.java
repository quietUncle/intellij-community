package org.jetbrains.debugger.memory.action.tracking;

import com.intellij.debugger.jdi.StackFrameProxyImpl;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import com.intellij.xdebugger.frame.XExecutionStack;
import com.intellij.xdebugger.impl.ui.tree.nodes.XValueNodeImpl;
import com.sun.jdi.ObjectReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.debugger.memory.action.DebuggerTreeAction;
import org.jetbrains.debugger.memory.component.CreationPositionTracker;

import java.util.List;

public class GetAllocationSourceAction extends DebuggerTreeAction {
  @Override
  public void update(AnActionEvent e) {
    e.getPresentation().setVisible(getStack(e) != null);
  }

  @Override
  protected void perform(XValueNodeImpl node, @NotNull String nodeName, AnActionEvent e) {
    List<StackFrameProxyImpl> stack = getStack(e);
    System.out.println(stack != null);
  }

  @Nullable
  private List<StackFrameProxyImpl> getStack(AnActionEvent e) {
    Project project = e.getProject();
    XValueNodeImpl selectedNode = getSelectedNode(e.getDataContext());
    ObjectReference ref = selectedNode != null ? getObjectReference(selectedNode) : null;
    if(project == null || ref == null) {
      return null;
    }

    // TODO: Wrong way: session may be not current
    XDebugSession session = XDebuggerManager.getInstance(project).getCurrentSession();
    return session == null ? null : CreationPositionTracker.getInstance(project).getStack(session, ref);
  }
}
