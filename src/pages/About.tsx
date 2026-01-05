import { Link } from 'react-router-dom';
import { Layout } from '@/components/Layout';
import { SEOHead } from '@/components/SEOHead';
import { Button } from '@/components/ui/button';
import { CheckCircle, Users, Briefcase, Shield, Target, Award, Heart } from 'lucide-react';

const About = () => {
  const team = [
    { name: 'Sarah Johnson', role: 'CEO & Founder', emoji: '👩‍💼' },
    { name: 'Michael Chen', role: 'CTO', emoji: '👨‍💻' },
    { name: 'Emily Davis', role: 'Head of Operations', emoji: '👩‍🔧' },
    { name: 'James Wilson', role: 'Customer Success', emoji: '👨‍💼' },
  ];

  const values = [
    { icon: Shield, title: 'Trust & Safety', description: 'We verify all job postings to protect our users from scams.' },
    { icon: Target, title: 'Quality Focus', description: 'Only legitimate, high-quality opportunities make it to our platform.' },
    { icon: Heart, title: 'User First', description: 'Everything we do is designed with our users\' success in mind.' },
    { icon: Award, title: 'Excellence', description: 'We strive for excellence in every aspect of our service.' },
  ];

  return (
    <Layout>
      <SEOHead
        title="About Us"
        description="Learn about OnlineJobsHub - your trusted platform for finding legitimate online work opportunities. Our mission is to connect job seekers with quality remote jobs."
        canonical="/about"
      />

      {/* Hero */}
      <section className="gradient-hero text-primary-foreground py-20 relative overflow-hidden">
        <div className="absolute inset-0 opacity-10">
          <div className="absolute top-20 left-10 w-72 h-72 bg-accent rounded-full blur-3xl" />
        </div>
        <div className="container mx-auto px-4 relative">
          <div className="max-w-3xl mx-auto text-center">
            <h1 className="text-4xl md:text-5xl font-bold mb-6">
              About OnlineJobsHub
            </h1>
            <p className="text-xl text-primary-foreground/80">
              Connecting talented individuals with legitimate remote work opportunities since 2020. 
              Our mission is to make online earning accessible to everyone.
            </p>
          </div>
        </div>
      </section>

      {/* Mission */}
      <section className="py-16 md:py-20">
        <div className="container mx-auto px-4">
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-12 items-center">
            <div>
              <h2 className="text-3xl md:text-4xl font-bold text-foreground mb-6">
                Our Mission
              </h2>
              <p className="text-muted-foreground mb-6 leading-relaxed">
                At OnlineJobsHub, we believe everyone deserves access to legitimate work opportunities 
                that fit their lifestyle. Whether you're a stay-at-home parent, a student, or someone 
                looking to supplement their income, we're here to help you find genuine online jobs.
              </p>
              <p className="text-muted-foreground mb-8 leading-relaxed">
                We carefully verify every job posting to ensure our users never fall victim to scams. 
                Our platform has helped over 50,000 people find meaningful remote work and earn a 
                sustainable income from home.
              </p>
              <div className="grid grid-cols-2 gap-4">
                {[
                  { value: '50,000+', label: 'Jobs Seekers Helped' },
                  { value: '10,000+', label: 'Active Job Listings' },
                  { value: '98%', label: 'User Satisfaction' },
                  { value: '24/7', label: 'Support Available' },
                ].map((stat) => (
                  <div key={stat.label} className="text-center p-4 bg-muted/50 rounded-xl">
                    <div className="text-2xl font-bold text-accent">{stat.value}</div>
                    <div className="text-sm text-muted-foreground">{stat.label}</div>
                  </div>
                ))}
              </div>
            </div>
            <div className="bg-gradient-to-br from-accent/20 to-primary/20 rounded-3xl p-10">
              <div className="space-y-6">
                {[
                  'Verified and legitimate job postings',
                  'No hidden fees or charges',
                  'Direct connection with employers',
                  'Secure application process',
                  'Regular payment protection tips',
                  'Free resources and guides',
                ].map((item) => (
                  <div key={item} className="flex items-center gap-3">
                    <CheckCircle className="h-5 w-5 text-accent shrink-0" />
                    <span className="text-foreground">{item}</span>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Values */}
      <section className="py-16 md:py-20 bg-muted/30">
        <div className="container mx-auto px-4">
          <div className="text-center mb-12">
            <h2 className="text-3xl md:text-4xl font-bold text-foreground mb-4">Our Values</h2>
            <p className="text-muted-foreground max-w-2xl mx-auto">
              The principles that guide everything we do
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            {values.map((value, index) => (
              <div 
                key={value.title}
                className="bg-card rounded-xl border border-border p-6 text-center animate-slide-up"
                style={{ animationDelay: `${index * 100}ms` }}
              >
                <div className="h-14 w-14 rounded-xl bg-accent/10 flex items-center justify-center mx-auto mb-4">
                  <value.icon className="h-7 w-7 text-accent" />
                </div>
                <h3 className="font-semibold text-foreground mb-2">{value.title}</h3>
                <p className="text-sm text-muted-foreground">{value.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Team */}
      <section className="py-16 md:py-20">
        <div className="container mx-auto px-4">
          <div className="text-center mb-12">
            <h2 className="text-3xl md:text-4xl font-bold text-foreground mb-4">Meet Our Team</h2>
            <p className="text-muted-foreground max-w-2xl mx-auto">
              The dedicated people behind OnlineJobsHub
            </p>
          </div>

          <div className="grid grid-cols-2 md:grid-cols-4 gap-6 max-w-4xl mx-auto">
            {team.map((member, index) => (
              <div 
                key={member.name}
                className="text-center animate-slide-up"
                style={{ animationDelay: `${index * 100}ms` }}
              >
                <div className="h-24 w-24 rounded-full bg-muted flex items-center justify-center mx-auto mb-4 text-4xl">
                  {member.emoji}
                </div>
                <h3 className="font-semibold text-foreground">{member.name}</h3>
                <p className="text-sm text-muted-foreground">{member.role}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="py-16 md:py-20 bg-muted/30">
        <div className="container mx-auto px-4 text-center">
          <h2 className="text-2xl md:text-3xl font-bold text-foreground mb-4">
            Ready to Find Your Dream Online Job?
          </h2>
          <p className="text-muted-foreground mb-8 max-w-xl mx-auto">
            Join thousands of people who have already started their remote work journey with us.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Button variant="cta" size="lg" asChild>
              <Link to="/register">Get Started Free</Link>
            </Button>
            <Button variant="outline" size="lg" asChild>
              <Link to="/jobs">Browse Jobs</Link>
            </Button>
          </div>
        </div>
      </section>
    </Layout>
  );
};

export default About;
