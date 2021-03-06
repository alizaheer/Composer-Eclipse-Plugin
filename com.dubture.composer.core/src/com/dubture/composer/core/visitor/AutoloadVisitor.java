package com.dubture.composer.core.visitor;

import org.eclipse.dltk.ast.references.VariableReference;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.php.internal.core.compiler.ast.nodes.ArrayCreation;
import org.eclipse.php.internal.core.compiler.ast.nodes.ArrayElement;
import org.eclipse.php.internal.core.compiler.ast.nodes.InfixExpression;
import org.eclipse.php.internal.core.compiler.ast.nodes.Scalar;
import org.eclipse.php.internal.core.compiler.ast.visitor.PHPASTVisitor;

import com.dubture.composer.core.model.ModelAccess;
import com.dubture.getcomposer.core.collection.Psr0;
import com.dubture.getcomposer.core.objects.Namespace;

@SuppressWarnings("restriction")
public class AutoloadVisitor extends PHPASTVisitor
{
    protected ISourceModule source;
    private NamespaceVisitor visitor;
    
    public AutoloadVisitor(ISourceModule source)
    {
        this.source = source;
    }


    @Override
    public boolean visit(ArrayCreation s) throws Exception
    {
        visitor = new NamespaceVisitor();
        s.traverse(visitor);
        
        Psr0 psr0 = visitor.getPsr0();
        
        if (psr0.size() > 0) {
        	ModelAccess.getInstance().updatePsr0(psr0, source.getScriptProject());
        }
        
        return true;
    }
    
    public Psr0 getPsr0() {
        
        if (visitor != null) {
            return visitor.getPsr0();
        }
        
        return null;
    }

    protected class NamespaceVisitor extends PHPASTVisitor {
        
        protected Psr0 psr0 = new Psr0();
        
        @Override
        public boolean visit(ArrayElement element) throws Exception
        {
            if (!(element.getKey() instanceof Scalar)) {
                return false;
            }
            
            if (element.getValue() instanceof InfixExpression) {
                Scalar namespace = (Scalar) element.getKey();
                Scalar path = (Scalar) ((InfixExpression) element.getValue()).getRight();
                VariableReference reference = (VariableReference) ((InfixExpression) element.getValue()).getLeft();
                extractPsr0(namespace, path, reference);
                return false;
            } else if(element.getValue() instanceof ArrayCreation) {
                Scalar namespace = (Scalar) element.getKey();
                ArrayCreation paths = (ArrayCreation) element.getValue();
                for (ArrayElement elem  : paths.getElements()) {
                	if (elem.getValue() instanceof InfixExpression) {
                		Scalar path = (Scalar) ((InfixExpression) elem.getValue()).getRight();
                        VariableReference reference = (VariableReference) ((InfixExpression) elem.getValue()).getLeft();
                        extractPsr0(namespace, path, reference);
                	}
                	return false;
                }
            }
            
            return true;
        }
        
        
        protected void extractPsr0(Scalar namespace, Scalar path, VariableReference reference) {
            String resourcePath = "";
            
            if ("$baseDir".equals(reference.getName())) {
                resourcePath = path.getValue().replace("'", "");
            } else if ("$vendorDir".equals(reference.getName())) {
                resourcePath = "vendor" + path.getValue().replace("'", "");
            }
            
            if (resourcePath.startsWith("/")) {
                resourcePath = resourcePath.replaceFirst("/", "");
            }
            
            String ns = namespace.getValue().replace("'", "").replace("\\\\", "\\");
            psr0.add(new Namespace(ns, resourcePath));
        }
        
        public Psr0 getPsr0()
        {
            return psr0;
        }
    }
}
