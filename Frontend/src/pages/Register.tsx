import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Layout } from '@/components/Layout';
import { SEOHead } from '@/components/SEOHead';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Checkbox } from '@/components/ui/checkbox';
import { toast } from 'sonner';

const Register = () => {
  const navigate = useNavigate();
  const [acceptTerms, setAcceptTerms] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!acceptTerms) {
      toast.error('Accept terms first');
      return;
    }

    setIsLoading(true);

    try {
      const response = await fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        const err = await response.json();
        throw new Error(err.message || 'Registration failed');
      }

      toast.success('Registered successfully');
      navigate('/login');
    } catch (err: any) {
      toast.error(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Layout>
      <SEOHead title="Register" />
      <form onSubmit={handleSubmit} className="max-w-md mx-auto py-16 space-y-4">
        <Label>Name</Label>
        <Input required onChange={(e) => setFormData({ ...formData, name: e.target.value })} />

        <Label>Email</Label>
        <Input type="email" required onChange={(e) => setFormData({ ...formData, email: e.target.value })} />

        <Label>Password</Label>
        <Input type="password" required minLength={8}
          onChange={(e) => setFormData({ ...formData, password: e.target.value })}
        />

        <div className="flex items-center gap-2">
          <Checkbox checked={acceptTerms} onCheckedChange={(v) => setAcceptTerms(v as boolean)} />
          <span>I accept terms</span>
        </div>

        <Button type="submit" disabled={isLoading}>
          {isLoading ? 'Creating...' : 'Register'}
        </Button>

        <p>
          Already have account? <Link to="/login">Login</Link>
        </p>
      </form>
    </Layout>
  );
};

export default Register;
